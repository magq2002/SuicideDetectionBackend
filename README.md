# Suicide Detection Backend

Este proyecto corresponde al backend del sistema de detección de ideas suicidas, desarrollado con Spring Boot. Integra procesamiento de lenguaje natural (PLN) utilizando GATE y Machine Learning con modelos avanzados.

## Requisitos Previos

- **Java**: JDK 17 o superior.
- **Maven**: Versión 3.6 o superior.
- **Base de Datos**: MySQL.
- **Python**: Versión 3.9.7 con entorno virtual configurado para GATE.
- **Modelo Preentrenado**: Archivo `modelo_final.pt`.

## Configuración
### Entorno Python (mi configuración local)
name: GATEPython
- pip:
    - charset-normalizer==3.4.0
    - filelock==3.0.12
    - fsspec==2024.10.0
    - gatenlp==1.0.8
    - huggingface-hub==0.26.2
    - idna==3.10
    - iobes==1.5.1
    - markupsafe==2.1.5
    - networkx==3.1
    - packaging==20.9
    - pyparsing==2.4.7
    - sacremoses==0.0.43
    - sortedcontainers==2.3.0
    - sympy==1.13.1
    - torch==2.4.1
    - torchaudio==2.5.1
    - torchvision==0.20.1
    - tqdm==4.66.6
    - transformers==4.3.2
    - typing-extensions==4.12.2


### Variables de Entorno
Crea un archivo `application.yml` en el directorio `src/main/resources` con las siguientes configuraciones:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/suicide_detection
    username: tu_usuario
    password: tu_contraseña
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

app:
  gate:
    scriptUrl: "src/main/resources/apply_prediction.py" //script python
    modelPathUrl: "C:/Users/magq2/Documents/entrenoBert/modelo_final.pt" //modelo entrenado
    pythonBinary: "C:/Users/magq2/.conda/envs/GATEPython/python.exe" //entorno virtual de python
server:
  port: 8080
