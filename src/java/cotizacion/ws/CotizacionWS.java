/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cotizacion.ws;

import cotizacion.dao.SociosDAO;
import cotizacion.dto.RtaCotizacionDTO;
import cotizacion.model.Socios;
import java.text.DecimalFormat;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author julian
 */
@WebService(serviceName = "CotizacionWS")
public class CotizacionWS {

    /**
     * Web service operation
     * @param montoSolicitado
     * @return 
     */
    @WebMethod(operationName = "generarCotizacion")
    public RtaCotizacionDTO generarCotizacion(@WebParam(name = "montoSolicitado") double montoSolicitado) {
        RtaCotizacionDTO rtaCotizacionDTO = new RtaCotizacionDTO();
        SociosDAO sociosDAO = new SociosDAO();
        Socios socio = sociosDAO.consultarSocioDisponible(montoSolicitado);

        if (socio != null) {
            DecimalFormat formatter = new DecimalFormat("#0.00");
            double valorCuotaMensual;
            double VF;//Valor Futuro
            double VA = montoSolicitado;//Valor Actual
            double i = socio.getTasaInteres();//Tasa de interés
            float porcentajeInteres = (float) (i/100);
            int n = 36;//Periodo de tiempo en Meses

            VF = VA * (1 + n * porcentajeInteres);
            valorCuotaMensual = VF/n;
            System.out.println("TASA INTERES: "+i);
            System.out.println("PORCENTAJE "+porcentajeInteres);
            System.out.println("VALOR FUTURO: "+formatter.format(VF));
            System.out.println("VALOR CUOTA MENSUAL: "+formatter.format(valorCuotaMensual));
            
            rtaCotizacionDTO.setNombreSocio(socio.getNombreSocio());
            rtaCotizacionDTO.setPagoTotalCredito(formatter.format(VF));
            rtaCotizacionDTO.setTasaInteres(""+socio.getTasaInteres());
            rtaCotizacionDTO.setValorCuotaMensual(formatter.format(valorCuotaMensual));
            
            rtaCotizacionDTO.setCodigoRespuesta("000");
            rtaCotizacionDTO.setMensajeRespuesta("Cotización realizada exitosamente.");
        }else{
            rtaCotizacionDTO.setCodigoRespuesta("005");
            rtaCotizacionDTO.setMensajeRespuesta("No hay socio disponible para el monto solicitado.");
        }
        return rtaCotizacionDTO;
    }
}
