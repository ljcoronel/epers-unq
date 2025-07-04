import serial
import time
import requests
import json
from datetime import datetime, timezone
import uuid
import urllib3
from datetime import datetime

# Esta version del script genera un archivo localmente que tiene que ser leido por Telegraf, no se puede usar directamente sin ninguna configuracion
# adjunto en la carpeta sensor/telegraf el archivo de configuracion de Telegraf

# Desactiva los warnings de SSL no verificados
urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)



def verificar_ubicacion(ubicacion_id):
    try:
        headers = {"Content-Type": "application/json"}
        url_ubicacion = f'https://sobrepersistiendo.onrender.com/ubicacion/{ubicacion_id}'
        res = requests.get(url=url_ubicacion, headers= headers,verify=False)
        if res.status_code == 404 : # si no existe la ubicacion actual creo una y me la asigno
            print("No existe ubicacion, creando ubicacion")
            return crearUbicacion(ubicacion_id)
        if res.status_code == 200:
            return res.json()["id"]
    except:
        raise Exception("NO se puede el id de la ubicacion") 


def crearUbicacion(ubicacion):
    headers = {"Content-Type": "application/json"}

    url_ubicacion = f'https://sobrepersistiendo.onrender.com/ubicacion'
    data = {"tipo": "CEMENTERIO",
            "nombre": f"Avellaneda_{uuid.uuid4().hex[:6]}",
            "int" : 100
            }        
    ub = requests.post(url=url_ubicacion, headers=headers, data=json.dumps(data),verify=False)
    if ub.status_code == 201:
        ub_res = ub.json()
        print("creada")
        print(ub_res)
        return ub_res["id"]
    else:
        print("error al crea ubicacion")


def spring_push_temp(json_data):
    url_sensor = "https://sobrepersistiendo.onrender.com/sensor"
    headers = {"Content-Type": "application/json"}

    print(json_data)  # json_data debería ser dict o list
    
    res = requests.post(url=url_sensor, json=json_data, headers=headers, verify=False)
    
    if res.status_code in (200, 201):
        print("Enviado..")
    else:
        print(res.json())
        print("Error al enviar data a Spring") 
        

        # response = requests.post(url, json=json_data, headers=headers)


def main():
    id_ubicacion_5 = 5
    id_ubicacion_10 = 10
    file = open("C:/Users/dpenalver/Documents/UNQ/EPERS/tp6/scripts/SensorData.txt",'a')
    file.write("time;ubicacion_id;sensor;value\n")
    # print(f'Id de ubicacion actual : {id_ubicacion}')
    ser = serial.Serial('COM5',250000)

    while True:
        decoded_data = ser.readline().decode('utf-8').strip() 
        # print(decoded_data)
        if not decoded_data:
                continue
        
        data = json.loads(decoded_data)
        print(data)
        sensor_ambiente = data['Temperaturas']['sensor1']
        sensor_manual = data['Temperaturas']['sensor2']
        # datos = {
        # "ubicacionId": id_ubicacion_10,
        # "temperatura": sensor_ambiente,
        # "timeStamp": datetime.now().strftime("%Y-%m-%dT%H:%M:%SZ"),
        # }
        print(f'{id_ubicacion_5};sensor1;{sensor_ambiente}\n')
        print(f'{id_ubicacion_10};sensor2;{sensor_manual}\n')

        file = open("C:/Users/dpenalver/Documents/UNQ/EPERS/tp6/scripts/SensorData.txt",'a')
        file.writelines(f'{datetime.now(timezone.utc)};{id_ubicacion_5};sensor1;{sensor_ambiente}\n');
        file.writelines(f'{datetime.now(timezone.utc)};{id_ubicacion_10};sensor2;{sensor_manual}\n')

        # time.sleep(0.5)

main()
