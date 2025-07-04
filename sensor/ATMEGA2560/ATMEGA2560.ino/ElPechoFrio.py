import tkinter as tk
import serial
import time
import threading
from influxdb_client import InfluxDBClient, Point, WritePrecision,WriteOptions
import json


bucket = "Juego"
org = "UNQ"
token = "vmNC2jBRWLU9cIgk4ckL3meLWMAErBWc4IgS10LgsPQ-wS0tyzTgMZ9kzPaUaG0vols5wxNW3wVoR50oulSq2w=="
url = "https://us-east-1-1.aws.cloud2.influxdata.com"
client = InfluxDBClient(url=url, token=token, org=org,verify_ssl=False)


write_api = client.write_api(write_options=WriteOptions(
    batch_size=1000,
    flush_interval=1000,
    jitter_interval=200,
    retry_interval=5000,
))

# Arduino serial
SERIAL_PORT = "COM5"  # O "/dev/ttyUSB0" según sistema
BAUD_RATE = 250000
DURATION = 10  # segundos

def medir():
    nombre = nombre_entry.get()
    ser = serial.Serial(SERIAL_PORT, BAUD_RATE, timeout=1)
    time.sleep(2)

    estado_label.config(text="Midiendo durante 10 segundos...")
    root.update()

    start = time.time()
    max_temp = -999
    min_temp = 999

    while True:
        elapsed = time.time() - start
        restante = DURATION - int(elapsed)

        if restante < 0:
            break

        tiempo_label.config(text=f"⏳ Tiempo restante: {restante} s")
        root.update()

        linea = ser.readline().decode('utf-8').strip()
        if linea:
            try:
                data = json.loads(linea)
                temp = data['Temperaturas']['sensor1']
                ahora = time.time_ns()

                max_temp = max(max_temp, temp)
                min_temp = min(min_temp, temp)

                punto = (
                    Point("Juego")
                    .tag("nombre", nombre)
                    .tag("sensor_id", "sensor1")
                    .field("temperatura", temp)
                    .time(ahora, WritePrecision.NS)
                )
                write_api.write(bucket=bucket, org=org, record=punto)

                estado_label.config(text=f'{nombre}: {temp:.1f}°C')
                root.update()

            except Exception as e:
                print("Error al procesar línea:", linea, "→", e)

    write_api.flush()
    ser.close()

    tiempo_label.config(text="✅ Finalizado")

def lanzar():
    thread = threading.Thread(target=medir)
    thread.start()



root = tk.Tk()
root.title("∯∬∳∎∌⊎≦≹≸∯⋣⋫⋬⍭⍬⍫⍼⍻⍾⎈⎈⏄ 👻😈👻")

descripcion_label = tk.Label(
    root,
    text="👻😈👻 👻 👻😈👻 \n\n" \
    "Estas ≸∯⋣⋫ dispuesto ≹a saber qu⍼e espiri⍼tu te acompaña en tu vida \n " \
    "Agar≸ra el sensor y medi tu temp⏄eratu⏄ra durante en 10 segundos.",
    font=("Segoe UI Symbol", 12),
    justify="center",
    wraplength=500
)
descripcion_label.grid(row=0, column=0, columnspan=2, pady=(10, 20))


tk.Label(root, text="Nom⌺⌻bre de⌽l M⌽edium⍕:", font=("Arial", 12)).grid(row=1, column=0)
nombre_entry = tk.Entry(root)
nombre_entry.grid(row=1, column=1)

btn = tk.Button(root, text="Comenzar Medición", command=lanzar)
btn.grid(row=2, column=0, columnspan=2, pady=5)



estado_label = tk.Label(root, text="", font=("Arial", 12))
estado_label.grid(row=3, column=0, columnspan=2, pady=10)

resultado_label = tk.Label(root, text="", font=("Arial", 14, "bold"))
resultado_label.grid(row=4, column=0, columnspan=2)

tiempo_label = tk.Label(root, text="", font=("Arial", 12))
tiempo_label.grid(row=5, column=0, columnspan=2)




root.mainloop()
