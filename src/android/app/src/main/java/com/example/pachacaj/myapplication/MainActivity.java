// ------------------------------------------------------------------
// Fichero: MainActivity.java
// Autor: Ferran Sansaloni Prats
// Fecha: 30/09/2025
// ------------------------------------------------------------------
// Descripción:
//   Esta clase gestiona la actividad principal de la aplicación Android
//   que permite detectar dispositivos BTLE,
//   mostrando la información en el Logcat y almacenando datos en la base
//   de datos mediante la clase BluetoothDataSender.
// ------------------------------------------------------------------

package com.example.pachacaj.myapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// ------------------------------------------------------------------
// Clase MainActivity
// ------------------------------------------------------------------
// Descripción:
//   Actividad principal de la app. Contiene la lógica para iniciar
//   Bluetooth, realizar escaneos BTLE, mostrar información de los
//   dispositivos detectados y almacenar los datos en la base de datos.
// ------------------------------------------------------------------
public class MainActivity extends AppCompatActivity {

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    // Etiqueta para los logs
    private static final String ETIQUETA_LOG = ">>>>";

    // Código de petición de permisos
    private static final int CODIGO_PETICION_PERMISOS = 11223344;

    // Escáner Bluetooth LE
    private BluetoothLeScanner elEscanner;

    // Callback del escaneo
    private ScanCallback callbackDelEscaneo = null;

    // --------------------------------------------------------------
    // Metodo: buscarTodosLosDispositivosBTLE
    // Descripción:
    //   Inicia un escaneo de todos los dispositivos BTLE disponibles.
    //   Configura un callback para procesar resultados individuales o
    //   en lote, y registra errores si falla el escaneo.
    // Diseño:
    //
    //              buscarTodosLosDispositivosBTLE()
    //                                     <--
    // --------------------------------------------------------------
    private void buscarTodosLosDispositivosBTLE() {
        // Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empieza ");

        // Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): instalamos scan callback ");

        // Configura callback para procesar los resultados
        this.callbackDelEscaneo = new ScanCallback() {
            // Se ejecuta cuando el escáner detecta un único dispositivo.
            // Muestra un mensaje en el log y envia el resultado del escaneo al metodo mostrarInformacionDispositivosBTLE
            @Override
            public void onScanResult( int callbackType, ScanResult resultado ) {
                super.onScanResult(callbackType, resultado);
                // Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanResult() ");

                mostrarInformacionDispositivoBTLE( resultado, false );
            }

            // Se deberia usar para cuando recibe una lista de resultados en lote. Pero por ahora solo escribe en el log, no procesa la lista results
            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onBatchScanResults() ");

            }

            // Se ejecuta si falla el escaneo. Muestra un mensaje en el log diciendo que ha fallado el escaneo.
            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanFailed() ");

            }
        };

        // Mensaje que avisa que se va a iniciar el escaneo de todos los dispositivos
        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empezamos a escanear ");

        // Si tiene los permisos ...
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                == PackageManager.PERMISSION_GRANTED) {
            // ... inicio el escaneo, ...
            this.elEscanner.startScan(this.callbackDelEscaneo);
        } else {
            // ... , si no, envío un mensaje de error
            Log.e(ETIQUETA_LOG, "No hay permiso para hacer startScan()");
        }


    } // ()

    // --------------------------------------------------------------
    // Metodo: mostrarInformacionDispositivoBTLE
    // Descripción:
    //   Procesa un ScanResult y muestra toda la información del dispositivo
    //   BTLE en el Log. También crea un objeto BluetoothDataSender para
    //   almacenar los datos en la base de datos.
    // Diseño:
    //   ScanResult: resultado --> mostrarInformacionDispositivoBTLE()
    //                                          <--
    // --------------------------------------------------------------
    private void mostrarInformacionDispositivoBTLE( ScanResult resultado, boolean guardarEnBBDD ) {

        BluetoothDevice bluetoothDevice = resultado.getDevice();
        byte[] bytes = resultado.getScanRecord().getBytes();
        int rssi = resultado.getRssi();

        Log.d("", " ");
        Log.d(ETIQUETA_LOG, " ****************************************************");
        Log.d(ETIQUETA_LOG, " ****** DISPOSITIVO DETECTADO BTLE ****************** ");
        Log.d(ETIQUETA_LOG, " ****************************************************");

        // Declaramos el nombre del dispositivo y su dirección
        String nombre = "Desconocido";
        String direccion = bluetoothDevice.getAddress();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                == PackageManager.PERMISSION_GRANTED) {
            // Cuando compruebe que tiene los permisos, cambiará el nombre de "Desconocido" por su nombre real.
            String nombreTemp = bluetoothDevice.getName();
            if (nombreTemp != null && !nombreTemp.isEmpty()) {
                nombre = nombreTemp;
            }
        } else {
            Log.d(ETIQUETA_LOG, "Permiso BLUETOOTH_CONNECT no concedido");
        }

        // Log.d(ETIQUETA_LOG, " toString = " + bluetoothDevice.toString());

        /*
        ParcelUuid[] puuids = bluetoothDevice.getUuids();
        if ( puuids.length >= 1 ) {
            //Log.d(ETIQUETA_LOG, " uuid = " + puuids[0].getUuid());
           // Log.d(ETIQUETA_LOG, " uuid = " + puuids[0].toString());
        }*/

        // Log.d(ETIQUETA_LOG, " dirección = " + bluetoothDevice.getAddress());
        // Log.d(ETIQUETA_LOG, " rssi = " + rssi );

        // Log.d(ETIQUETA_LOG, " bytes = " + new String(bytes));
        // Log.d(ETIQUETA_LOG, " bytes (" + bytes.length + ") = " + Utilidades.bytesToHexString(bytes));


        // Creamos la trama de bytes
        TramaIBeacon tib = new TramaIBeacon(bytes);

        // Variables con los datos de la trama
        String prefijo = Utilidades.bytesToHexString(tib.getPrefijo());
        String advFlags = Utilidades.bytesToHexString(tib.getAdvFlags());
        String advHeader = Utilidades.bytesToHexString(tib.getAdvHeader());
        String companyID = Utilidades.bytesToHexString(tib.getCompanyID());
        String iBeaconType = Integer.toHexString(tib.getiBeaconType());
        int iBeaconLength = tib.getiBeaconLength();
        String iBeaconLengthHex = Integer.toHexString(iBeaconLength);
        String uuidHex = Utilidades.bytesToHexString(tib.getUUID());

        byte[] uuidBytes = tib.getUUID(); // 16 bytes del UUID
        ByteBuffer bb = ByteBuffer.wrap(uuidBytes);
        long msb = bb.getLong();
        long lsb = bb.getLong();
        UUID uuid = new UUID(msb, lsb);

        String uuidString = uuid.toString();

        String majorHex = Utilidades.bytesToHexString(tib.getMajor());
        int majorInt = Utilidades.bytesToInt(tib.getMajor());
        String minorHex = Utilidades.bytesToHexString(tib.getMinor());
        int minorInt = Utilidades.bytesToInt(tib.getMinor());
        String txPowerHex = Integer.toHexString(tib.getTxPower());
        int txPower = tib.getTxPower();
        String bytesHex = Utilidades.bytesToHexString(bytes);

        if (uuid.equals("EPSG-GTI-PROY-3A")) {
            nombre = "fistro";
        }

        // Enviados los datos de la trama por el logcat
        // Log.d(ETIQUETA_LOG, " ----------------------------------------------------");
        Log.d(ETIQUETA_LOG, " nombre = " + nombre);
        // Log.d(ETIQUETA_LOG, " prefijo  = " + prefijo);
        Log.d(ETIQUETA_LOG, " rssi  = " + rssi);
        // Log.d(ETIQUETA_LOG, "          advFlags = " + advFlags);
        // Log.d(ETIQUETA_LOG, "          advHeader = " + advHeader);
        // Log.d(ETIQUETA_LOG, "          iBeacon type = " + iBeaconType);
        // Log.d(ETIQUETA_LOG, "          iBeacon length 0x = " + iBeaconLengthHex + " ( "
        //         + iBeaconLength + " ) ");
        // Log.d(ETIQUETA_LOG, " uuid  = " + uuidHex);
        Log.d(ETIQUETA_LOG, " uuid  = " + uuidString);
        Log.d(ETIQUETA_LOG, " major  = " + majorHex + "( "
                + majorInt + " ) ");
        Log.d(ETIQUETA_LOG, " minor  = " + minorHex + "( "
                + minorInt + " ) ");
        Log.d(ETIQUETA_LOG, " companyID = " + companyID);
        Log.d(ETIQUETA_LOG, " txPower  = " + txPowerHex + " ( " + txPower + " )");
        // Log.d(ETIQUETA_LOG, " ****************************************************");


        // Guardar datos en bbdd

        // Log.d(">>>>>>", "Dispositivo detectado: " + nombre + " - RSSI: " + rssi);

        // Si el booleano guardarEnBBDD estrue creo objeto con los datos que vamos a guardar
        if(guardarEnBBDD) {
            BluetoothDataSender dataSender = new BluetoothDataSender(
                    nombre,
                    uuidString,
                    rssi,
                    majorInt,
                    minorInt
            );

            dataSender.guardarMedida();
        } else {
            Log.d(ETIQUETA_LOG, " [INFO] Solo se muestra, no se guarda en la base de datos.");
        }

    } // ()

    // --------------------------------------------------------------
    // Metodo: buscarEsteDispositivoBTLE
    // Descripción:
    //   Inicia el escaneo filtrando por un nombre de dispositivo específico.
    // Diseño:
    //   final String: dispositivoBuscado --> buscarEsteDispositivoBTLE()
    //                                                  <--
    // --------------------------------------------------------------
    private void buscarEsteDispositivoBTLE(final String dispositivoBuscado ) {
        Log.d(ETIQUETA_LOG, " buscarEsteDispositivoBTLE(): empieza ");

        // Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): instalamos scan callback ");


        // super.onScanResult(ScanSettings.SCAN_MODE_LOW_LATENCY, result); para ahorro de energía

        this.callbackDelEscaneo = new ScanCallback() {

            // Cuando detecta un dispositivo le pasa el resultado del escaneo al metodo que muestra la información
            @Override
            public void onScanResult( int callbackType, ScanResult resultado ) {
                super.onScanResult(callbackType, resultado);
                // Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanResult() ");

                mostrarInformacionDispositivoBTLE( resultado, true );
            } // ()

            // Metodo para cuando lleguen varios dispositivos a la vez. No hace nada de momento.
            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                // Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onBatchScanResults() ");

            } // ()

            // Si falla el escaneo muestra por el log que ha habido un error en el ecaneo.
            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                // Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanFailed() ");

            } // ()
        };

        // Mensaje avisando que empieza a buscar nuestro dispositivo
        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): empezamos a escanear buscando: " + dispositivoBuscado );
        //Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): empezamos a escanear buscando: " + dispositivoBuscado
        //      + " -> " + Utilidades.stringToUUID( dispositivoBuscado ) );

        // Si se han concedido todos los permisos...
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                == PackageManager.PERMISSION_GRANTED) {

            // ... crea una lista llamada filtros y creamos un filtro que busca dispositivos cuyo nombre Bluetooth coincida con el que le
            // pasamos al parametro "dispositivoBuscado"
            List<ScanFilter> filtros = new ArrayList<>();
            filtros.add(new ScanFilter.Builder().setDeviceName(dispositivoBuscado).build());

            // Añade la configuración
            ScanSettings settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // escaneo rápido
                    .build();

            // Y iniciamos el escaneo pasandole el filtro, la configuración y el callback del escaneo
            this.elEscanner.startScan(filtros, settings, this.callbackDelEscaneo);

        } else {
            // Error
            Log.e(ETIQUETA_LOG, "No hay permiso para hacer startScan()");
        }

    } // ()

    // --------------------------------------------------------------
    // Metodo: detenerBusquedaDispositivosBTLE
    // Descripción:
    //   Detiene cualquier escaneo BTLE activo.
    // Diseño:
    //                  detenerBusquedaDispositivosBTLE
    //                                  <--
    // --------------------------------------------------------------
    private void detenerBusquedaDispositivosBTLE() {

        // Si no recibe calback del escaneo no hace nada.
        if ( this.callbackDelEscaneo == null ) {
            return;
        }

        // Compruebo que tenga los permisos necesarios para que funcione el bluetooth
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                == PackageManager.PERMISSION_GRANTED) {
            // Si tengo los permisos llamo a "stopScan()"
            this.elEscanner.stopScan(this.callbackDelEscaneo);
        } else {
            // Si no tengo los permisos mando un error
            Log.e(ETIQUETA_LOG, "No hay permiso para hacer startScan()");
        }

        this.callbackDelEscaneo = null;

    } // ()

    // --------------------------------------------------------------
    // v: View -->
    //              botonBuscarDispositivosBTLEPulsado()
    //                  <--
    //
    // Avisa que le hemos dado al botón y llama a buscar todos los buscar todos los dispositivos btle.
    // --------------------------------------------------------------
    public void botonBuscarDispositivosBTLEPulsado( View v ) {
        Log.d(ETIQUETA_LOG, " boton buscar dispositivos BTLE Pulsado" );
        this.buscarTodosLosDispositivosBTLE();
    } // ()

    // --------------------------------------------------------------
    // v: View -->
    //              botonBuscarNuestroDispositivoBTLEPulsado()
    //                  <--
    //
    // Avisa que le hemos dado al botón y llama a buscar este dispositivo y le pasa el nombre que queremos buscar.
    // --------------------------------------------------------------
    public void botonBuscarNuestroDispositivoBTLEPulsado( View v ) {
        Log.d(ETIQUETA_LOG, " boton nuestro dispositivo BTLE Pulsado" );
        //this.buscarEsteDispositivoBTLE( Utilidades.stringToUUID( "EPSG-GTI-PROY-3A" ) );

        //this.buscarEsteDispositivoBTLE( "EPSG-GTI-PROY-3A" );
        this.buscarEsteDispositivoBTLE( "fistro" );

    } // ()

    // --------------------------------------------------------------
    // v: View -->
    //              botonDetenerBusquedaDispositivosBTLEPulsado()
    //                  <--
    //
    // Avisa que le hemos dado al botón y llama a detener la busqueda de dispositivos btle.
    // --------------------------------------------------------------
    public void botonDetenerBusquedaDispositivosBTLEPulsado( View v ) {
        Log.d(ETIQUETA_LOG, " boton detener busqueda dispositivos BTLE Pulsado" );
        this.detenerBusquedaDispositivosBTLE();
    } // ()

    // --------------------------------------------------------------
    // Metodo: inicializarBlueTooth
    // Descripción:
    //   Inicializa el adaptador Bluetooth y el escáner BTLE.
    //   Gestiona la solicitud de permisos y habilita Bluetooth si es necesario.
    // Diseño:
    //                      inicializarBluetooth()
    //                           <--
    // --------------------------------------------------------------
    private void inicializarBlueTooth() {
        // Crea un adaptador bluetooth
        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();

        // Si el adaptador es es nulo manda un error.
        if (bta == null) {
            Log.e(ETIQUETA_LOG, "Este dispositivo no soporta Bluetooth");
            return;
        }

        // Pedir permisos según la versión
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // API 31+
            String[] permisos = {
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };

            boolean todosConcedidos = true;
            for (String p : permisos) {
                if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                    todosConcedidos = false;
                    break;
                }
            }

            if (!todosConcedidos) {
                ActivityCompat.requestPermissions(this, permisos, CODIGO_PETICION_PERMISOS);
            } else {
                if (!bta.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 1);
                }
            }
        } else {
            // Android 11 o menor
            if (!bta.isEnabled()) {
                bta.enable();
            }

            // Pedir ubicación si falta
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        CODIGO_PETICION_PERMISOS
                );
            }
        }

        // Inicializar escáner BTLE
        this.elEscanner = bta.getBluetoothLeScanner();
        if (this.elEscanner == null) {
            Log.e(ETIQUETA_LOG, "No se ha podido obtener el escáner BTLE");
        }
    } // ()

    // --------------------------------------------------------------
    // Metodo: onCreate()
    // Descripción: Primer metodo en ejecutarse, su funcion es ejecutar la actividad y inicializar el blutooth
    // Diseño:      Establece la vista principal del layout, inicializa Bluetooth
    //              y escribe mensajes de inicio y fin en el log.
    // Parámetros:
    //      - savedInstanceState: Bundle → estado anterior de la actividad.
    // --------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Avisamos que se va a iniciar el onCreate y llamamos a iniciar blutooth y despues avisamos que ha terminado de ejecutarse el onCreate
        Log.d(ETIQUETA_LOG, " onCreate(): empieza ");

        // Inicializamos el bluetooth
        inicializarBlueTooth();

        Log.d(ETIQUETA_LOG, " onCreate(): termina ");

    } // onCreate()

    // --------------------------------------------------------------
    // Metodo: onRequestPermissionsResult()
    // Descripción: Gestiona la respuesta del sistema tras solicitar permisos al usuario.
    //              Si se conceden, vuelve a inicializar Bluetooth.
    // Diseño:
    //          int: requestCode, String[]: permissions, int[]: grantResults --> onRequestPermissionsResult()
    //                                                                                 <--
    // --------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Si el codigo de respspuesta corresponde al codigo de la peticion de permisos
        if (requestCode == CODIGO_PETICION_PERMISOS) {
            // Pone el booleano todosConcedidos en true, es decir que se han concedido todos los permisos
            boolean todosConcedidos = true;
            // Recorre la lista de los permisos y si alguno no se ha concedido...
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    // pone el booleano todosConcedidos en false y rompe la ejecucion.
                    todosConcedidos = false;
                    break;
                }
            }

            // Aqui confirma si se han concedido todos los permisos, si es asi, muestra "Permisos concedidos" si no, muestra
            // "Permisos NO concedidos"
            if (todosConcedidos) {
                // Log.d(ETIQUETA_LOG, "Permisos concedidos");
                inicializarBlueTooth(); // ahora que hay permisos, inicializamos
            } else {
                Log.d(ETIQUETA_LOG, "Permisos NO concedidos");
            }
        }
    } // ()



} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------