package com.example.fundtransferservice.service;

import com.example.fundtransferservice.client.FundTransferRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FeesCalculationService {

    public static final double fraisServiceNotification = 20.0;
    private final FundTransferRestClient fundTransferRestClient;

    public Map<String,Double> calculFraisDonneurOrdre(double montantSaisi, double fraisTransfert, boolean notification){
        Map<String,Double> frais = new HashMap<>();

        if(notification){
            fraisTransfert+= fraisServiceNotification;
        }
        // ? : this amount should be debited from the ( donneur d'ordre ) account
        double montantTotalOperation = montantSaisi + fraisTransfert;
        frais.put("montantTotalOperation",montantTotalOperation);

        // ? : this amount should be added to the ( benificary ) account
        double montantTransferer = montantSaisi;
        frais.put("montantTransferer",montantTransferer);

        return frais;
    }
    public Map<String,Double> calculFraisBeneficiaire(double montantSaisi, double fraisTransfert,boolean notification){

        Map<String,Double> frais = new HashMap<>();
        if(notification){
            fraisTransfert+= fraisServiceNotification;
        }

        // ? : this amount should be debited from the ( donneur d'ordre ) account
        double montantTotalOperation = montantSaisi;
        frais.put("montantTotalOperation",montantTotalOperation);


        // ? : this amount should be added to the ( benificary ) account
        double montantTransferer = montantSaisi - fraisTransfert;
        frais.put("montantTransferer",montantTransferer);

        return frais;
    }
    public Map<String,Double> calculFraisPartages(double montantSaisi, double fraisTransfert,boolean notification){

        Map<String,Double> frais = new HashMap<>();
        if(notification){
            fraisTransfert+= fraisServiceNotification;
        }
        // ? : this amount should be debited from the ( donneur d'ordre ) account
        double montantTotalOperation = montantSaisi + 0.5 * fraisTransfert;
        frais.put("montantTotalOperation",montantTotalOperation);

        // ? : this amount should be added to the ( benificary ) account
        double montantObtenu = montantSaisi -  0.5 *fraisTransfert;
        frais.put("montantTransferer",montantObtenu);
        return frais;


    }
}
