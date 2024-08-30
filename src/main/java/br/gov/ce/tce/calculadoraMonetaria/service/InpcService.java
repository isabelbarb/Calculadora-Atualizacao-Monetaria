package br.gov.ce.tce.calculadoraMonetaria.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class InpcService {

    @Autowired
    private InpcLoader inpcLoader;

    @Autowired
    private ApiServiceClient apiServiceClient;

    public double getInpc(String periodo) {
        Double inpc = inpcLoader.getInpc(periodo);

        if (inpc == null) {
            inpc = fetchInpcFromApi(periodo);
            inpcLoader.addInpc(periodo, inpc);
        }
        return inpc;
    }

    private double fetchInpcFromApi(String periodo) {
        try {
            return Double.parseDouble(apiServiceClient.getInpcData(periodo).getFirst()
                    .resultados().getFirst()
                    .series().getFirst()
                    .serie().get(periodo));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar o INPC para o período: " + periodo, e);
        }
    }

}