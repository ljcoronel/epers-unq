from influxdb_client import InfluxDBClient, Point, WritePrecision, WriteOptions

import serial
import json
from datetime import datetime, timezone
import urllib3

urllib3.disable_warnings()

# Config
bucket = "sensores"
org = "Epers_Sobre_2025"
token = "yv8Uf_l0x-jBoEqfwN6P0TzqE-YyYuLxyGPsLbKPVpE92KagaGzbVAGht-9QrLYDQQwuiIHqemE5ASHs78vjTg=="
url = "https://us-east-1-1.aws.cloud2.influxdata.com"




client = InfluxDBClient(url=url, token=token, org=org, verify_ssl=False)
write_api = client.write_api(write_options=WriteOptions(
    batch_size=500,        # envía cuando hay 500 puntos
    flush_interval=5000,   # o cada 5 segundos (lo que ocurra primero)
    jitter_interval=200,
    retry_interval=5000
))

ser = serial.Serial('COM5', 250000)

def construir_punto(sensor, ubicacion_id, valor):
    return (
        Point("temperatura")
        .tag("sensor", sensor)
        .tag("ubicacion_id", str(ubicacion_id))
        .field("value", float(valor))
        .time(datetime.now(timezone.utc), WritePrecision.S)
    )

while True:
    try:
        linea = ser.readline().decode('utf-8').strip()
        if not linea:
            continue

        data = json.loads(linea)
        sensor1 = data['Temperaturas']['sensor1']
        sensor2 = data['Temperaturas']['sensor2']

        p1 = construir_punto("sensor1", 5, sensor1)
        p2 = construir_punto("sensor2", 10, sensor2)

        write_api.write(bucket=bucket, org=org, record=[p1, p2])
        print(f"[✔] Encolados sensor1={sensor1}, sensor2={sensor2}")

    except Exception as e:
        print(f"[✖] Error en lectura o encolar datos: {e}")
