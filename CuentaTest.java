package org.sarmiento.junitapp.ejemplos.models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.sarmiento.junitapp.ejemplos.exception.DineroInsuficienteException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assumptions.*;
import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {
    Cuenta cuenta;

    private TestInfo testInfo;
    private TestReporter testReporter;
    @BeforeEach
    void initMetodoTest(TestInfo testInfo, TestReporter testReporter){
        this.cuenta = new Cuenta("Miguel", new BigDecimal("1000.12345"));
        this.testInfo = testInfo;
        this.testReporter = testReporter;
        System.out.println("Iniciando el metodo.");
        System.out.println(" ejecutando: " + testInfo.getDisplayName() + " " + testInfo.getTestMethod().orElse(null).getName()
                + " con las etiquetas " + testInfo.getTags());
    }

/*    @AfterEach
    void tearDown() {
        System.out.println("Finalizando el metodo de prueba");
    }*/
    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando el test");
    }


/*    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el test");
    }*/

    @Tag("Cuenta")
    @Test
    @DisplayName("Probando nombre de la cuenta corriente !")
    void testNombreCuenta() {
        System.out.println(testInfo.getTags());
        if (testInfo.getTags().contains("Cuenta")){
            System.out.println("Hacer algo con la etiqueta cuenta");
        }
        //cuenta.setPersona("Miguel");
        String esperado = "Miguel";
        String real = cuenta.getPersona();
        assertNotNull(real, "La cuenta no puede ser nuela"); //este mensaje saldrá cuando de error
        assertEquals(esperado, real, " El nombre de la cuenta no es el que se esperaba"); //este mensaje saldrá cuando de error
        assertEquals("Miguel", real, "Nombre cuenta esperada debe ser igual a la real"); //este mensaje saldrá cuando de error
    }

    @Tag("Cuenta")
    @Test
    @DisplayName("Probando el saldo de la cuenta corriente, que no sea null, mayor que cero, valor esperado")
    void testSaldoCuenta(){

        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
    }

    @Tag("Cuenta")
    @Test
    @DisplayName("Testeando referencia que sean iguales con el metodo equals")
    void testReferenciaCuenta() {
        Cuenta cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));

        //assertNotEquals(cuenta2, cuenta); //Nos estamos refiriendo a espacio en memoria.
        assertEquals(cuenta2, cuenta); //Nos estamos refiriendo a espacio en memoria.
    }

    @Tag("Cuenta")
    @Test
    @DisplayName("Comprobando la funcion de 'debito' y de 'credito'")
    void testDebitoCuenta() {
        cuenta.debito(new BigDecimal(100));

        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue()); //Nos devuelve la parte entera
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Tag("Cuenta")
    @Test
    @DisplayName("Me quede sin idea pero Itachi es el mejor")
    void testCrebitoCuenta() {
        cuenta.credito(new BigDecimal(100));

        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue()); //Nos devuelve la parte entera
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Tag("Cuenta")
    @Test
    @DisplayName("Comprobando la cantidad de dinero en la cuenta")
    void testDineroInsuficienteExceptionCuenta() {
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero insuficiente";

        assertEquals(esperado, actual);
    }

    @Tag("Cuenta")
    @Tag("Banco")
    @Test
    @DisplayName("Testeando")
    void testTransferirDineroCuentas() {
        Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Miguel", new BigDecimal("1500.8989"));
        Banco banco = new Banco();
        banco.setNombre("Banco del estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal("500"));

        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());
    }

    @Tag("Cuenta")
    @Tag("Banco")
    @Test
    @DisplayName("Mas testeo")
    void testRelacionBancoCuentas() {
        Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Miguel", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("Banco del estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal("500"));

        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());
        assertEquals(2, banco.getCuentas().size());
        assertEquals("Banco del estado", cuenta1.getBanco().getNombre());
        assertEquals("Miguel", banco.getCuentas().stream().filter(cuenta -> cuenta.getPersona()
                        .equals("Miguel")).findFirst()
                .get().getPersona());
        assertTrue(banco.getCuentas().stream().anyMatch(c -> c.getPersona()
                .equals("Miguel")));
    }

    @Test
    @EnabledOnOs(OS.WINDOWS) //Esto solo habilita el test a WINDOWS
    void testSoloWindows() {
    }

/*    @Test
    @EnabledOnJre(JRE.JAVA_8) //Esto solo habilita el test a java 8
    void testSoloJdk8() {
    }*/

    @Test
    void imprimirSystemProperties() {
        Properties properties = System.getProperties();
        properties.forEach((k, v) ->System.out.println(k + ":" + v));
    }


    @Test
    @EnabledIfSystemProperty(named = "user.name", matches = "MiguelSarmientoLevy")
    void testJavaVersion() {
    }

    @Test
    @DisplayName("Test saldo cuenta dev")
    void testSaldoCuentaDev() {
        boolean esDev= "dev".equals(System.getProperty("ENV"));
        assumingThat(esDev, ()-> {
            assumeTrue(esDev);
            assertNotNull(cuenta.getSaldo());
        });
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
    }
    @RepeatedTest(value = 5, name= "Repeticion numero {currentRepetition} de {totalRepetitions}")
    @DisplayName("Comprobando la función de 'debito'")
    void testDebitoCuenta2(RepetitionInfo info) {
        if (info.getCurrentRepetition() ==3){
            System.out.println("Estamos en la repeticion " + info.getCurrentRepetition());
        }
        cuenta.debito(new BigDecimal(100));

        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue()); //Nos devuelve la parte entera
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Tag("Param")
    @ParameterizedTest(name = "numero {index} ejecutando con valor {0}-{argumentsWithNames}")
    @ValueSource(strings = {"100", "200", "300", "500", "700", "1000.12345"})
    void testDebitoCuentaOtro(String monto) {

        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Tag("Param")
    @ParameterizedTest(name = "numero {index} ejecutando con valor {0}-{argumentsWithNames}")
    @CsvSource({"1, 100", "2, 200", "3, 300", "4, 500", "5, 700", "6, 1000.12345"})
    void testDebitoCuentaOtroCsvSource(String index, String monto) {
        System.out.println(index + " -> " + monto);
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Tag("Param")
    @ParameterizedTest(name = "numero {index} ejecutando con valor {0}-{argumentsWithNames}")
    @CsvSource({"200, 100, John, Andres", "250, 200, Pepe, Pepe", "300, 300, maria, Maria", "510, 500, Pepa, pepa", "750, 700, Lucas, Luca", "1000.12345, 1000.12345, Cata, Cata"})
    void testDebitoCuentaOtroCsvSource2(String saldo, String monto, String esperado, String actual) {
        System.out.println(saldo + " -> " + monto);
        cuenta.setSaldo(new BigDecimal(saldo));
        cuenta.debito(new BigDecimal(monto));
        cuenta.setPersona(actual);
        assertNotNull(cuenta.getSaldo());
        assertNotNull(cuenta.getPersona());
        assertEquals(esperado, actual);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Tag("Param")
    @ParameterizedTest(name = "numero {index} ejecutando con valor {0}-{argumentsWithNames}")
    @CsvFileSource(resources = "/data.csv")
    void testDebitoCuentaOtroCsvFileSource(String monto) {
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Tag("Param")
    @ParameterizedTest(name = "numero {index} ejecutando con valor {0}-{argumentsWithNames}")
    @CsvFileSource(resources = "/data2.csv")
    void testDebitoCuentaOtroCsvFileSource2(String saldo, String monto, String esperado, String actual) {
        cuenta.setSaldo(new BigDecimal(saldo));
        cuenta.debito(new BigDecimal(monto));
        cuenta.setPersona(actual);

        assertNotNull(cuenta.getSaldo());
        assertNotNull(cuenta.getPersona());
        assertEquals(esperado, actual);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Tag("Param")
    @ParameterizedTest(name = "numero {index} ejecutando con valor {0}-{argumentsWithNames}")
    @MethodSource("montoList")
    void testDebitoCuentaOtroMethodSource(String monto) {
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }
    private static List<String> montoList(){
        return Arrays.asList("100", "200", "300", "500", "700", "1000.12345");
    }

    @Nested
    @Tag("timeout")
    class EjemploTimeoutTest{
        @Test
        @Timeout(value = 1000 ,unit = TimeUnit.MILLISECONDS)
        void pruebaTimeout() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(900);
        }

        @Test
        @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
        void pruebaTimeout2() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(900);
        }

        @Test
        void testTimeoutAssertions() {
            assertTimeout(Duration.ofSeconds(5), ()->{
                TimeUnit.MILLISECONDS.sleep(4000);
            });
        }
    }
}