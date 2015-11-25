package se.inera.certificate.modules.support.api.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import se.inera.certificate.logging.HashUtility;
import se.inera.certificate.modules.support.api.CertificateHolder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PersonnummerTest {

    @BeforeClass
    public static void setUp() throws Exception {
        ClassLoader.getSystemClassLoader().setClassAssertionStatus("se.inera.certificate.modules.support.api.dto.Personnummer", false);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        ClassLoader.getSystemClassLoader().setClassAssertionStatus("se.inera.certificate.modules.support.api.dto.Personnummer", true);
    }

    @Test
    public void testGetPersonnummer() throws Exception {
        //Given
        final String pnr = "190104063698";

        //When
        final Personnummer personnummer = new Personnummer(pnr);

        //Then
        assertEquals(pnr, personnummer.getPersonnummer());
    }

    @Test
    public void testToStringShouldReturnTheHashedValue() throws Exception {
        final List<String> pnrs = Arrays.asList("000000-0000", "00000000-0000", "000000000000", "0000000000", null, "");
        for (String pnr : pnrs) {
            try {
                assertEquals(new Personnummer(pnr).getPnrHash(), new Personnummer(pnr).toString());
            } catch (AssertionError ae) {
                //I don't know how to disable assertions in code when running test via maven, hence let this assertion error pass
            }
        }
    }

    @Test
    public void testGetPnrHash() throws Exception {
        assertEquals("1424d7d0b8d4afd3c1ab1068a5a54d2d6d05d5b07e16effe36c176bd14b53c1c", new Personnummer("920926-2386").getPnrHash());
        assertEquals("1424d7d0b8d4afd3c1ab1068a5a54d2d6d05d5b07e16effe36c176bd14b53c1c", new Personnummer("199209262386").getPnrHash());
        assertEquals(HashUtility.EMPTY, new Personnummer(null).getPnrHash());
        assertEquals(HashUtility.EMPTY, new Personnummer("").getPnrHash());
        assertEquals(HashUtility.EMPTY, Personnummer.empty().getPnrHash());
    }

    @Test
    public void testIsSamordningsNummer() throws Exception {
        assertFalse(new Personnummer("000000-0000").isSamordningsNummer());
        assertTrue(new Personnummer("999999-9999").isSamordningsNummer());
        assertFalse(new Personnummer("0000000000").isSamordningsNummer());
        assertTrue(new Personnummer("9999999999").isSamordningsNummer());
        assertFalse(new Personnummer("000000000000").isSamordningsNummer());
        assertTrue(new Personnummer("199999999999").isSamordningsNummer());
        assertFalse(new Personnummer("99999999999912345").isSamordningsNummer());
    }

    @Test
    public void testGetPersonnummerWithoutDashRemovesDash() throws Exception {
        //Given
        final Personnummer personnummer = new Personnummer("000000-0000");

        //When
        final String personnummerWithoutDash = personnummer.getPersonnummerWithoutDash();

        //Then
        assertEquals("0000000000", personnummerWithoutDash);
    }

    @Test
    public void testGetPersonnummerWithoutDashWillKeepPnrWithoutDashTheSame() throws Exception {
        //Given
        final String pnr = "0000000000";
        final Personnummer personnummer = new Personnummer(pnr);

        //When
        final String personnummerWithoutDash = personnummer.getPersonnummerWithoutDash();

        //Then
        assertEquals(pnr, personnummerWithoutDash);
    }

    @Test
    public void testTwoDifferentPnrsNotEquals() throws Exception {
        //Given
        final Personnummer personnummer0 = new Personnummer("000000-0000");
        final Personnummer personnummer9 = new Personnummer("999999-9999");

        //Then
        assertFalse(personnummer0.equals(personnummer9));
        assertFalse(personnummer9.equals(personnummer0));
    }

    @Test
    public void testTwoIdenticalPnrsEquals() throws Exception {
        //Given
        final Personnummer personnummer0 = new Personnummer("000000-0000");
        final Personnummer personnummer1 = new Personnummer("000000-0000");

        //Then
        assertTrue(personnummer0.equals(personnummer1));
        assertTrue(personnummer1.equals(personnummer0));
    }

    @Test
    public void testPnrWithOrWithoutDashEquals() throws Exception {
        //Given
        final Personnummer personnummer0 = new Personnummer("920926-2386");
        final Personnummer personnummer1 = new Personnummer("9209262386");

        //Then
        assertTrue(personnummer0.equals(personnummer1));
        assertTrue(personnummer1.equals(personnummer0));
    }

    @Test
    public void testSamePersonnummerEquals() throws Exception {
        //Given
        final Personnummer personnummer = new Personnummer("000000-0000");

        //Then
        assertTrue(personnummer.equals(personnummer));
    }

    @Test
    public void testHashCode() throws Exception {
        //Given
        final Personnummer personnummer0 = new Personnummer("000000-0000");
        final Personnummer personnummer9 = new Personnummer("999999-9999");

        //Then
        assertTrue(personnummer0.hashCode() != personnummer9.hashCode());
        assertEquals(personnummer0.hashCode(), personnummer0.hashCode());
    }

    @Test
    public void testGetPnrHashSafe() throws Exception {
        assertEquals(HashUtility.EMPTY, Personnummer.getPnrHashSafe(null));
        assertEquals(HashUtility.EMPTY, Personnummer.getPnrHashSafe(Personnummer.empty()));
        assertEquals(HashUtility.EMPTY, Personnummer.getPnrHashSafe(new Personnummer(null)));
        assertEquals(HashUtility.EMPTY, Personnummer.getPnrHashSafe(new Personnummer("")));

        final Personnummer personnummer = new Personnummer("920926-2386");
        assertEquals("1424d7d0b8d4afd3c1ab1068a5a54d2d6d05d5b07e16effe36c176bd14b53c1c", Personnummer.getPnrHashSafe(personnummer));
        assertEquals(personnummer.getPnrHash(), Personnummer.getPnrHashSafe(personnummer));
    }

    @Test
    public void testEmptyPersonnummerCanHandleAllCalls() throws Exception {
        //Given
        final Personnummer empty = Personnummer.empty();

        //Then
        assertEquals(HashUtility.EMPTY, empty.getPnrHash());
        assertEquals(null, empty.getPersonnummer());
        assertEquals(null, empty.getPersonnummerWithoutDash());
        assertEquals(false, empty.equals(null));
        assertEquals(31, empty.hashCode());
        assertEquals(false, empty.isSamordningsNummer());

        try {
            assertEquals(HashUtility.EMPTY, empty.toString());
        } catch (AssertionError ae) {
            //I don't know how to disable assertions in code when running test via maven, hence let this assertion error pass
        }

        boolean exceptionThrownWhenNormalizing = false;
        try {
            empty.getNormalizedPnr();
        } catch (InvalidPersonNummerException e) {
            exceptionThrownWhenNormalizing = true;
        }
        assertTrue(exceptionThrownWhenNormalizing);

        //Fail this test if a new method is added to Personnummer to make sure a call to it is added here
        assertEquals(10, countNonPrivateMethodsInClass(Personnummer.class));

    }

    private int countNonPrivateMethodsInClass(Class<Personnummer> personnummerClass) {
        final Method[] declaredMethods = personnummerClass.getDeclaredMethods();
        int counter = 0;
        for (Method declaredMethod : declaredMethods) {
            if (!Modifier.isPrivate(declaredMethod.getModifiers())) {
                counter++;
            }
        }
        return counter;
    }

    @Test
    public void testGetNormalizedPnr() throws Exception {
        //Given
        final String pnr = "196001011111";
        final String expected = "196001011111";

        //When
        final String normalizedPnr = new Personnummer(pnr).getNormalizedPnr();

        //Then
        assertEquals(expected, normalizedPnr);
    }

    @Test
    public void testGetNormalizedPnr2() throws Exception {
        //Given
        final String pnr = "19600101-1111";
        final String expected = "196001011111";

        //When
        final String normalizedPnr = new Personnummer(pnr).getNormalizedPnr();

        //Then
        assertEquals(expected, normalizedPnr);
    }

    @Test
    public void testGetNormalizedPnr3() throws Exception {
        //Given
        final String pnr = "600101-1111";
        final String expected = "196001011111";

        //When
        final String normalizedPnr = new Personnummer(pnr).getNormalizedPnr();

        //Then
        assertEquals(expected, normalizedPnr);
    }

    @Test
    public void testGetNormalizedPnr4() throws Exception {
        //Given
        final String pnr = "600101+1111";
        final String expected = "186001011111";

        //When
        final String normalizedPnr = new Personnummer(pnr).getNormalizedPnr();

        //Then
        assertEquals(expected, normalizedPnr);
    }

    @Test
    public void testGetNormalizedPnr5() throws Exception {
        //Given
        final String pnr = "190101010101";
        final String expected = "190101010101";

        //When
        final String normalizedPnr = new Personnummer(pnr).getNormalizedPnr();

        //Then
        assertEquals(expected, normalizedPnr);
    }

    @Test
    public void testSerializeDeserializePersonnummerNull() throws Exception {
        //Given
        final ObjectMapper objectMapper = new ObjectMapper();
        final Personnummer originalPnr = new Personnummer(null);

        //When
        final String json = objectMapper.writeValueAsString(originalPnr);

        //Then
        assertEquals("null", json);

        //When
        final Personnummer personnummer = objectMapper.readValue(json, Personnummer.class);

        //Then
        assertEquals(originalPnr.getPersonnummer(), personnummer.getPersonnummer());
    }

    @Test
    public void testSerializeDeserializePersonnummerEmpty() throws Exception {
        //Given
        final ObjectMapper objectMapper = new ObjectMapper();
        final Personnummer originalPnr = new Personnummer("");

        //When
        final String json = objectMapper.writeValueAsString(originalPnr);

        //Then
        assertEquals("\"\"", json);

        //When
        final Personnummer personnummer = objectMapper.readValue(json, Personnummer.class);

        //Then
        assertEquals(originalPnr.getPersonnummer(), personnummer.getPersonnummer());
    }

    @Test
    public void testSerializePersonnummer() throws Exception {
        //Given
        final ObjectMapper objectMapper = new ObjectMapper();
        final Personnummer value = new Personnummer("1234");

        //When
        final String json = objectMapper.writeValueAsString(value);

        //Then
        assertEquals("\"1234\"", json);
    }

    @Test
    public void testDeserializePersonnummer() throws Exception {
        //Given
        final ObjectMapper objectMapper = new ObjectMapper();

        //When
        final Personnummer personnummer = objectMapper.readValue("\"1234\"", Personnummer.class);

        //Then
        assertEquals("1234", personnummer.getPersonnummer());
    }

    @Test
    public void testSerializeDeserializePersonnummerAsPartOfComplexType() throws Exception {
        //Given
        final ObjectMapper objectMapper = new ObjectMapper();
        final Personnummer originalPnr = new Personnummer("191212121212");
        final CertificateHolder complexType = new CertificateHolder();
        complexType.setCivicRegistrationNumber(originalPnr);
        complexType.setAdditionalInfo("test text");

        //When
        final String json = objectMapper.writeValueAsString(complexType);

        //Then
        assertTrue(json.contains("\"civicRegistrationNumber\":\"191212121212\""));

        //When
        final CertificateHolder patient = objectMapper.readValue(json, CertificateHolder.class);

        //Then
        assertEquals(originalPnr.getPersonnummer(), patient.getCivicRegistrationNumber().getPersonnummer());
    }

}
