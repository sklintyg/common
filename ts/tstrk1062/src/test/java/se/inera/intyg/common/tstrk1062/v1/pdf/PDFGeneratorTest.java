/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.common.tstrk1062.v1.pdf;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import org.junit.Test;

import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.schemas.contract.Personnummer;

public class PDFGeneratorTest {

    @Test
    public void testGeneratePdf() throws Exception {
        final SortedMap<String, String> mockedTextMap = mock(SortedMap.class);
        doReturn("Text").when(mockedTextMap).get(any());

        final String expectedIntygsId = "ad5faeed-3899-410a-9094-4f0e4b8efdca";
        final String expectedJsonModel = "{\"id\":\"ad5faeed-3899-410a-9094-4f0e4b8efdca\",\"grundData\":{\"signeringsdatum\":\"2019-02-27T09:28:19.227\",\"skapadAv\":{\"personId\":\"TSTNMT2321000156-1079\",\"fullstandigtNamn\":\"Arnold Johansson\",\"forskrivarKod\":\"0000000\",\"befattningar\":[],\"specialiteter\":[],\"vardenhet\":{\"enhetsid\":\"TSTNMT2321000156-1077\",\"enhetsnamn\":\"NMT vg3 ve1\",\"postadress\":\"NMT gata 3\",\"postnummer\":\"12345\",\"postort\":\"Testhult\",\"telefonnummer\":\"0101112131416\",\"epost\":\"enhet3@webcert.invalid.se\",\"vardgivare\":{\"vardgivarid\":\"TSTNMT2321000156-102Q\",\"vardgivarnamn\":\"NMT vg3\"},\"arbetsplatsKod\":\"1234567890\"}},\"patient\":{\"personId\":\"191212121212\",\"fullstandigtNamn\":\"Tolvan Tolvansson\",\"fornamn\":\"Tolvan\",\"efternamn\":\"Tolvansson\",\"postadress\":\"Svensson, Storgatan 1, PL 1234\",\"postnummer\":\"12345\",\"postort\":\"Småmåla\",\"sekretessmarkering\":false,\"avliden\":false,\"samordningsNummer\":false},\"relation\":{}},\"textVersion\":\"1.0\",\"signature\":\"PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/PjxkczpTaWduYXR1cmUgeG1sbnM6ZHNmPSJodHRwOi8vd3d3LnczLm9yZy8yMDAyLzA2L3htbGRzaWctZmlsdGVyMiIgeG1sbnM6ZHM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyMiPjxkczpTaWduZWRJbmZvPjxkczpDYW5vbmljYWxpemF0aW9uTWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8xMC94bWwtZXhjLWMxNG4jIi8+PGRzOlNpZ25hdHVyZU1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZHNpZy1tb3JlI3JzYS1zaGEyNTYiLz48ZHM6UmVmZXJlbmNlIFVSST0iIj48ZHM6VHJhbnNmb3Jtcz48ZHM6VHJhbnNmb3JtIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnI2VudmVsb3BlZC1zaWduYXR1cmUiLz48ZHM6VHJhbnNmb3JtIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvVFIvMTk5OS9SRUMteHNsdC0xOTk5MTExNiI+PHhzbDpzdHlsZXNoZWV0IHZlcnNpb249IjEuMCIgeG1sbnM6eHNsPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L1hTTC9UcmFuc2Zvcm0iPjx4c2w6b3V0cHV0IGluZGVudD0ibm8iIG9taXQteG1sLWRlY2xhcmF0aW9uPSJ5ZXMiLz48eHNsOnN0cmlwLXNwYWNlIGVsZW1lbnRzPSIqIi8+PHhzbDp0ZW1wbGF0ZSBtYXRjaD0iKiI+PHhzbDplbGVtZW50IG5hbWU9Intsb2NhbC1uYW1lKC4pfSI+PHhzbDphcHBseS10ZW1wbGF0ZXMgc2VsZWN0PSJub2RlKCl8QCoiLz48L3hzbDplbGVtZW50PjwveHNsOnRlbXBsYXRlPjx4c2w6dGVtcGxhdGUgbWF0Y2g9IkAqIj48eHNsOmNvcHkvPjwveHNsOnRlbXBsYXRlPjwveHNsOnN0eWxlc2hlZXQ+PC9kczpUcmFuc2Zvcm0+PGRzOlRyYW5zZm9ybSBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDIvMDYveG1sZHNpZy1maWx0ZXIyIj48ZHNmOlhQYXRoIEZpbHRlcj0iaW50ZXJzZWN0Ij4vL2V4dGVuc2lvblt0ZXh0KCk9J2FkNWZhZWVkLTM4OTktNDEwYS05MDk0LTRmMGU0YjhlZmRjYSddLy4uLy4uPC9kc2Y6WFBhdGg+PC9kczpUcmFuc2Zvcm0+PGRzOlRyYW5zZm9ybSBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDIvMDYveG1sZHNpZy1maWx0ZXIyIj48ZHNmOlhQYXRoIEZpbHRlcj0ic3VidHJhY3QiPi8vKltsb2NhbC1uYW1lKCkgPSAnc2tpY2thdFRpZHB1bmt0J118Ly8qW2xvY2FsLW5hbWUoKSA9ICdyZWxhdGlvbiddfC8vKltsb2NhbC1uYW1lKCkgPSAnc3RhdHVzJ118Ly8qW2xvY2FsLW5hbWUoKSA9ICd1bmRlcnNrcmlmdCddPC9kc2Y6WFBhdGg+PC9kczpUcmFuc2Zvcm0+PGRzOlRyYW5zZm9ybSBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMTAveG1sLWV4Yy1jMTRuIyIvPjwvZHM6VHJhbnNmb3Jtcz48ZHM6RGlnZXN0TWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8wNC94bWxlbmMjc2hhMjU2Ii8+PGRzOkRpZ2VzdFZhbHVlPndpR2Q0MUx1Zld6SHV4Snl0OVZtUHY3dW5Ec2Z1RjZQZXQ1ZlVUTzVRTlE9PC9kczpEaWdlc3RWYWx1ZT48L2RzOlJlZmVyZW5jZT48L2RzOlNpZ25lZEluZm8+PGRzOlNpZ25hdHVyZVZhbHVlPmNhUzVOODc1QnRCQWxpK25qQmFyOWJ6cm9XZllyMWlPVStOUkQwV255K0xFZlhxQVROY2c0dG5NSHpFMHlhbzB6N1lxVm96a2VDZkdFREMvNzFZWGphTWFtY1JtdUI5K3RlRzU4UlpKUkw3d0cwNEkzeTBUR0xzaDBTdUIraXYwMnJnMTdKcmJmZzJWSFdtazZQNGNvdVlSd2ZLRzJyd2JmajJueDJ5OVljRT08L2RzOlNpZ25hdHVyZVZhbHVlPjxkczpLZXlJbmZvPjxkczpYNTA5RGF0YT48ZHM6WDUwOUNlcnRpZmljYXRlPk1JSUNTakNDQWJPZ0F3SUJBZ0lFQ1hFQmpEQU5CZ2txaGtpRzl3MEJBUXNGQURCWU1Rc3dDUVlEVlFRR0V3SlRSVEVOTUFzR0ExVUVDQk1FVkdWemRERU5NQXNHQTFVRUJ4TUVWR1Z6ZERFTk1Bc0dBMVVFQ2hNRVZHVnpkREVOTUFzR0ExVUVDeE1FVkdWemRERU5NQXNHQTFVRUF4TUVWR1Z6ZERBZUZ3MHhPREV3TVRVeE16QTBORFJhRncweU9ERXdNVEl4TXpBME5EUmFNRmd4Q3pBSkJnTlZCQVlUQWxORk1RMHdDd1lEVlFRSUV3UlVaWE4wTVEwd0N3WURWUVFIRXdSVVpYTjBNUTB3Q3dZRFZRUUtFd1JVWlhOME1RMHdDd1lEVlFRTEV3UlVaWE4wTVEwd0N3WURWUVFERXdSVVpYTjBNSUdmTUEwR0NTcUdTSWIzRFFFQkFRVUFBNEdOQURDQmlRS0JnUUNJeGYxQzEyaEJxa2VHMEsrc2MySHR5ZmdaODFGT0Rra1dydGF2YmZGYlNJUEV1L21qYnhBVkVJbHdGaytrRk05cy9SRTlTQlBHZHZrSnNSYVIzTHMrSlZwc1ZHeFluak43Ri9LK3FxazdMU1JZNld5N1F6UDljWTV1Q09EZTMrWmVDRkxkYTdXTE9qTERSRHErdkhoUFRqZ1AwVVFSb3pJQ2plUnVvd3g2OVFJREFRQUJveUV3SHpBZEJnTlZIUTRFRmdRVTkyUm1YSXZSQko2OEgvVktRU3hBRWJFZTVlUXdEUVlKS29aSWh2Y05BUUVMQlFBRGdZRUFTNzZucDN3bjdxVWZCK25RTG5mK0JNTmJsTmFnb2c1bE93NVFDbkxLNi9rZ3BObnRoM0hjQmlqcVAvR2dZdDczR09PTDFLSlhyUjd2SnUrajdzSzEwT1ltVXpaUFUxWkFiRmppZXF4L1hhTnNUMTVDeENLUzBuandXakFjMitOOGFzTi9OSDNkcEVaNHQvU3ZnM2lOcWUyWFJOUm1wT1VlYmMxN1Z4cWhoSkE9PC9kczpYNTA5Q2VydGlmaWNhdGU+PC9kczpYNTA5RGF0YT48L2RzOktleUluZm8+PC9kczpTaWduYXR1cmU+\",\"intygAvser\":{\"behorigheter\":[{\"type\":\"IAV11\",\"selected\":true},{\"type\":\"IAV12\",\"selected\":false},{\"type\":\"IAV13\",\"selected\":false},{\"type\":\"IAV14\",\"selected\":false},{\"type\":\"IAV15\",\"selected\":false},{\"type\":\"IAV16\",\"selected\":false},{\"type\":\"IAV17\",\"selected\":false},{\"type\":\"IAV1\",\"selected\":false},{\"type\":\"IAV2\",\"selected\":false},{\"type\":\"IAV3\",\"selected\":false},{\"type\":\"IAV4\",\"selected\":false},{\"type\":\"IAV5\",\"selected\":false},{\"type\":\"IAV6\",\"selected\":false},{\"type\":\"IAV7\",\"selected\":false},{\"type\":\"IAV8\",\"selected\":false},{\"type\":\"IAV9\",\"selected\":false}]},\"idKontroll\":{\"typ\":\"ID_KORT\"},\"diagnosRegistrering\":{\"typ\":\"DIAGNOS_KODAD\"},\"diagnosKodad\":[{\"diagnosKod\":\"A01\",\"diagnosKodSystem\":\"ICD_10_SE\",\"diagnosBeskrivning\":\"Tyfoidfeber och paratyfoidfeber\",\"diagnosArtal\":\"2018\"}],\"diagnosFritext\":{},\"lakemedelsbehandling\":{\"harHaft\":false},\"bedomningAvSymptom\":\"Test\",\"prognosTillstand\":{\"typ\":\"JA\"},\"bedomning\":{\"uppfyllerBehorighetskrav\":[{\"type\":\"VAR12\",\"selected\":true},{\"type\":\"VAR13\",\"selected\":false},{\"type\":\"VAR14\",\"selected\":false},{\"type\":\"VAR15\",\"selected\":false},{\"type\":\"VAR16\",\"selected\":false},{\"type\":\"VAR17\",\"selected\":false},{\"type\":\"VAR18\",\"selected\":false},{\"type\":\"VAR1\",\"selected\":false},{\"type\":\"VAR2\",\"selected\":false},{\"type\":\"VAR3\",\"selected\":false},{\"type\":\"VAR4\",\"selected\":false},{\"type\":\"VAR5\",\"selected\":false},{\"type\":\"VAR6\",\"selected\":false},{\"type\":\"VAR7\",\"selected\":false},{\"type\":\"VAR8\",\"selected\":false},{\"type\":\"VAR9\",\"selected\":false},{\"type\":\"VAR11\",\"selected\":false}]},\"typ\":\"tstrk1062\"}";
        final Personnummer expectedPersonId = Personnummer.createPersonnummer("19121212-1212").get();
        final IntygTexts expectedIntygTexts = new IntygTexts("1.0", "tstrk1062", null, null, mockedTextMap, null, null);
        final List expectedStatuses = new ArrayList<Status>();
        final ApplicationOrigin expectedApplicationOrigin = ApplicationOrigin.WEBCERT;
        final UtkastStatus expectedUtkastStatus = UtkastStatus.DRAFT_COMPLETE;

        final PdfResponse actualPdfResponse = new PdfGenerator().generatePdf(expectedIntygsId, expectedJsonModel, expectedPersonId,
            expectedIntygTexts, expectedStatuses, expectedApplicationOrigin, expectedUtkastStatus);

        assertNotNull("PdfResponse should not be null", actualPdfResponse);
        assertNotNull("Data should not be null", actualPdfResponse.getPdfData());
        assertNotNull("Filename should not be null", actualPdfResponse.getFilename());
        assertFalse("Filename should not be empty", actualPdfResponse.getFilename().isEmpty());
    }
}
