#include <math.h> // para log()
#include <LiquidCrystal.h>
#define TEMP_AMB_1 A8  // Cambia según el pin donde esté conectado
#define TEMP_AMB_2 A10
#define BUZZER_PIN 18 // Pin de parlante
#define UMBRAL_DIFERENCIA 10
#define SERIES_RESISTOR 4700          // resistencia fija del divisor en ohmios
#define NOMINAL_RESISTANCE 100000    // resistencia nominal del termistor a 25ºC
#define NOMINAL_TEMPERATURE 25       // temperatura nominal (en ºC)
#define B_COEFFICIENT 3950           // coeficiente Beta del termistor
#define ADC_MAX 1023   
#define BAUDIOS 250000// valor máximo ADC para 10 bits
#define BUTTON_PIN 19
#define X_MIN_BUTTON  22

//Const
bool ultimoEstado = HIGH;
unsigned long tiempoInicio = 0;
long tiempoPresionado = 0;
bool estadoAnterior = false;
bool bajando = false;
unsigned long tiempoDisplay = 0;
unsigned long tiempoSerial = 0;
bool estadoParlante = false;
bool sonidoActivo = false;
const unsigned long intervaloDisplay = 500;
const unsigned long intervaloSerial = 100;


// Configura los pines del LCD: RS, E, D4, D5, D6, D7
LiquidCrystal lcd(20, 17, 16, 21, 5, 6);

byte PARLANTEon[8] = {
  B00010,
  B01001,
  B00101,
  B10101,
  B10101,
  B00101,
  B01001,
  B00010
};

byte PARLANTEOff[8] = {
  B10001,
  B11001,
  B01111,
  B11111,
  B11111,
  B01111,
  B11001,
  B10001
};

byte CAMPANA1[8] = {
  B00100,
  B01110,
  B01110,
  B01110,
  B11111,
  B00100,
  B00000,
  B00100,
};
byte DEMONIO[8] = {
  B10101,
  B01110,
  B11111,
  B10101,
  B11111,
  B01110,
  B10101,
  B01010
};
byte ANGEL[8] = {
  B01010, 
  B11111, 
  B01110, 
  B10101, 
  B01110, 
  B01110, 
  B00100, 
  B01010
};
byte P[8] = {
B11111,
B10001,
B10001,
B10001,
B11111,
B10000,
B10000,
B00000,
};
byte cara[8] = {
B00000,
B10001,
B00000,
B00000,
B10001,
B01110,
B00000,
};
byte cuerpo[8] = {
B01110,
B01110,
B00100,
B11111,
B00100,
B01010,
B10001,
B00000,
};





void crearCaracteresEspeciales(){
  lcd.createChar (0,PARLANTEon);
  lcd.createChar (1,PARLANTEOff);
  lcd.createChar (2,DEMONIO);
  lcd.createChar (3,ANGEL);
 }

  
void setup() {
  Serial.begin(BAUDIOS);
  pinMode(X_MIN_BUTTON, INPUT_PULLUP);
  crearCaracteresEspeciales();
  lcd.begin(20, 4);  // LCD 16 columnas, 4 filas
  pinMode(BUZZER_PIN, OUTPUT); // setup parlante
  pinMode(BUTTON_PIN, INPUT_PULLUP); // El botón está normalmente en HIGH
  lcd.print("Iniciando...");
  
}

void leerDiplayBotonBoton() {

  bool estadoActual = digitalRead(BUTTON_PIN);

  if (!estadoActual)  {
    sonidoActivo=!sonidoActivo;
  }
}

float leerTemperatura(int pin) {
  int adcValue = analogRead(pin);

  if (adcValue == 0) {
    return -1000; // error para evitar división por cero
  }

  // Calcula la resistencia del termistor en ohmios
  float resistance = SERIES_RESISTOR / ((ADC_MAX / (float)adcValue) - 1);

  // Calcula la temperatura usando la fórmula Beta
  float steinhart;
  steinhart = resistance / NOMINAL_RESISTANCE;  // (R/R0)
  steinhart = log(steinhart);                    // ln(R/R0)
  steinhart /= B_COEFFICIENT;                    // ln(R/R0)/B
  steinhart += 1.0 / (NOMINAL_TEMPERATURE + 273.15); // + 1/T0
  steinhart = 1.0 / steinhart;                   // invertir
  steinhart -= 273.15;                           // convertir K a ºC

  return steinhart;
}


String temperaturaAJson(float temp1, float temp2) {
  
  String json = "{ \"Temperaturas\": {";
  json += "\"sensor1\": ";
  json += String(temp1);
  json += ",\"sensor2\": ";
  json += String(temp2);
  json += "}}";

  return json;
   
}

void dibujarParlante(){
   lcd.setCursor(19,0);
  if (sonidoActivo) {
    lcd.write(byte(0)); // Parlante Tachado
  }else { 
    sonidoActivo= false;
    lcd.write(byte(1)); // Parlante Activo
    }
};

  
void showInfodisplay(float temp1, float temp2, bool estado_parlante) {

  float delta = temp1 - temp2;
  char buf_temp1[8], buf_temp2[8], buf_del[8];

  dtostrf(temp1, 5, 2, buf_temp1);
  dtostrf(temp2,    5, 2, buf_temp2);
  dtostrf(delta,    5, 2, buf_del);

  lcd.clear();                     
  lcd.setCursor(0, 0);              
  lcd.print("Legion RDJ");
  dibujarParlante();
  

  lcd.setCursor(0, 1);              
  lcd.print("Antena UNQ");
  lcd.print("  ");

  lcd.print("t:");
  lcd.print(buf_del);
 

  lcd.setCursor(0, 2);
  lcd.print("T:");
  lcd.print(buf_temp1);
  lcd.print(" Ta:");
  lcd.print(buf_temp2);

  if ( abs(delta) < 5 ) {
    lcd.setCursor(0, 3);
    lcd.print("Buscando Espiritus!!");  
  }
  if (delta < -5 ) {
    lcd.setCursor(0, 3);
    lcd.print("Demonio Encontrado ");  
    lcd.write(byte(2));  
   }

   if (delta > 5 ) {
    lcd.setCursor(0, 3);
    lcd.print("Angel Encontrado ");
    lcd.write(byte(3));  
   }

}

void sonarParlante(float delta){
  if (sonidoActivo){
  if (delta >= UMBRAL_DIFERENCIA) {
    tone(BUZZER_PIN, 2000, 500);  // Sonido constante
  } 
  else if (delta >=2.0 && delta < UMBRAL_DIFERENCIA) {
    tone(BUZZER_PIN, 1000 + ((int)delta) * ((int)delta),100  );
 
  }
//  else {
//   noTone(BUZZER_PIN);
//   }
  }
}

float estadoBoton() {
  bool presionado = digitalRead(X_MIN_BUTTON) == HIGH;  // NC: presionado = HIGH

  if (presionado && !estadoAnterior) {
    // Se acaba de presionar
    tiempoInicio = millis() - tiempoPresionado;  // Continúa desde donde estaba si venía bajando
    estadoAnterior = true;
    bajando = false;
  }

  if (presionado && estadoAnterior) {
    tiempoPresionado = millis() - tiempoInicio;
    return tiempoPresionado;
  }

  if (!presionado && estadoAnterior) {
    // Se acaba de soltar
    estadoAnterior = false;
    bajando = true;
  }

  if (bajando && tiempoPresionado > 0) {
      static unsigned long ultimoTiempo = millis();
  unsigned long ahora = millis();
  unsigned long delta = ahora - ultimoTiempo;
  ultimoTiempo = ahora;

  tiempoPresionado -= delta/1.5;  // Resta según el tiempo real transcurrido
  if (tiempoPresionado < 0) tiempoPresionado = 0;
  return tiempoPresionado;
  }

  return 0;
}
 
void loop() {
  unsigned long ahora = millis();
  
  float temp1 = leerTemperatura(TEMP_AMB_1);
  float temp2 = leerTemperatura(TEMP_AMB_2);
  temp1 = temp1 -(estadoBoton()/750); //Si se presiona el boton va disminuyendo la temperatura en 1,5° por segundo
  float sumarATemp = 0;
  float delta = abs(temp1 - temp2);

  if (ahora - tiempoDisplay >= intervaloDisplay) {
    tiempoDisplay = ahora;
    leerDiplayBotonBoton();
    showInfodisplay(temp1, temp2, estadoParlante);
    sonarParlante(delta);
  }
   if (ahora - tiempoSerial >= intervaloSerial) {
    tiempoSerial = ahora;
    String salida = temperaturaAJson(temp1, temp2);
    Serial.println(salida);
   }
}
