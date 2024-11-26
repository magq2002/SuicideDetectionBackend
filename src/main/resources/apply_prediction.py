import os
import torch
import logging
from gatenlp import Document, GateNlpPr, interact
from transformers import AutoTokenizer, AutoModel
from torch import nn
import torch.nn.functional as F

# Configuración básica del logger
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class EnhancedCNN_LSTM_Attention(nn.Module):
    def __init__(self, model_name, num_labels=2, dropout_rate=0.3):
        super(EnhancedCNN_LSTM_Attention, self).__init__()
        self.bert = AutoModel.from_pretrained(model_name)
        self.conv1 = nn.Conv1d(768, 128, kernel_size=2, padding='same')
        self.conv2 = nn.Conv1d(768, 128, kernel_size=3, padding='same')
        self.conv3 = nn.Conv1d(768, 128, kernel_size=4, padding='same')
        self.batch_norm = nn.BatchNorm1d(384)
        self.lstm = nn.LSTM(input_size=384, hidden_size=128, num_layers=2,
                            batch_first=True, bidirectional=True, dropout=dropout_rate)
        self.attention = nn.MultiheadAttention(256, num_heads=8, dropout=dropout_rate)
        self.fc1 = nn.Linear(256, 128)
        self.fc2 = nn.Linear(128, num_labels)
        self.layer_norm = nn.LayerNorm(256)
        self.dropout = nn.Dropout(dropout_rate)

    def forward(self, input_ids, attention_mask):
        bert_output = self.bert(input_ids=input_ids, attention_mask=attention_mask)
        x = bert_output.last_hidden_state.permute(0, 2, 1)
        conv_out = torch.cat([F.relu(self.conv1(x)), F.relu(self.conv2(x)), F.relu(self.conv3(x))], dim=1)
        conv_out = self.batch_norm(conv_out).permute(0, 2, 1)
        lstm_out, _ = self.lstm(conv_out)
        attention_out, _ = self.attention(lstm_out.permute(1, 0, 2), lstm_out.permute(1, 0, 2), lstm_out.permute(1, 0, 2))
        attention_out = self.layer_norm(lstm_out + attention_out.permute(1, 0, 2))
        pooled = self.dropout(torch.mean(attention_out, dim=1))
        return self.fc2(F.relu(self.fc1(pooled)))

@GateNlpPr
class SuicideDetectionPr:
    def __init__(self):
        a = 1

    def start(self, **kwargs):
        try:
            model_name = kwargs.get('model_name', "dccuchile/bert-base-spanish-wwm-cased")
            model_path = kwargs.get('model_path', 'src/main/resources/modeloBertDatasetPropio.pt')
            gpu = kwargs.get('gpu', 'False').lower() == 'true'
            self.device = torch.device('cuda' if torch.cuda.is_available() and gpu else 'cpu')

            self.tokenizer = AutoTokenizer.from_pretrained(model_name)
            self.model = EnhancedCNN_LSTM_Attention(model_name).to(self.device)

            if os.path.exists(model_path):
                self.model.load_state_dict(torch.load(model_path, map_location=self.device))
                self.model.eval()
                logger.info(f"Modelo cargado desde {model_path}")
            else:
                raise FileNotFoundError(f"Archivo del modelo no encontrado en {model_path}")
        except Exception as e:
            logger.error(f"Error inicializando SuicideDetectionPr: {e}")

    def preprocesar_frase(self, frase):
        try:
            encoding = self.tokenizer(
                frase, max_length=128, padding='max_length', truncation=True, return_tensors='pt'
            )
            return encoding['input_ids'].to(self.device), encoding['attention_mask'].to(self.device)
        except Exception as e:
            logger.error(f"Error en preprocesar_frase: {e}")
            return None, None

    def predecir_frase(self, frase):
        try:
            input_ids, attention_mask = self.preprocesar_frase(frase)
            if input_ids is not None:
                with torch.no_grad():
                    logits = self.model(input_ids, attention_mask)
                    return 'Ideación Suicida' if torch.argmax(logits, dim=1).item() == 1 else 'No Ideación Suicida'
        except Exception as e:
            logger.error(f"Error en predecir_frase: {e}")
        return "Error en predicción"

    def __call__(self, doc: Document, **kwargs):
        try:
            doc_text = doc.text
            prediccion = self.predecir_frase(doc_text)
            working_set = doc.annset(kwargs.get('workingSet', ''))
            working_set.add(0, len(doc_text), "SuicidePrediction", {"type": prediccion})
            logger.info(f"Anotación creada en el documento: {prediccion}")
        except Exception as e:
            logger.error(f"Error en __call__: {e}")

if __name__ == '__main__':
    interact()
