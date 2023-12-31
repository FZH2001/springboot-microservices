package com.example.fundtransferservice.service;

import org.springframework.stereotype.Service;

@Service
public class FeesCalculationService {

    public static final double fraisServiceNotification = 20.0;

    public void calculFraisDonneurOrdre(double montantSaisi, double fraisTransfert,boolean notification){

        if(notification){
            fraisTransfert+= fraisServiceNotification;
        }
        // ? : this amount should be debited from the ( donneur d'ordre ) account
        double montantTotalOperation = montantSaisi + fraisTransfert;
        // ? : this amount should be added to the ( benificary ) account
        double montantTransferer = montantSaisi;
    }
    public void calculFraisBeneficiaire(double montantSaisi, double fraisTransfert,boolean notification){
        if(notification){
            fraisTransfert+= fraisServiceNotification;
        }

        // ? : this amount should be debited from the ( donneur d'ordre ) account
        double montantTotalOperation = montantSaisi;
        // ? : this amount should be added to the ( benificary ) account
        double montantTransferer = montantSaisi - fraisTransfert;
    }
    public void calculFraisPartages(double montantSaisi, double fraisTransfert,boolean notification){
        if(notification){
            fraisTransfert+= fraisServiceNotification;
        }
        // ? : this amount should be debited from the ( donneur d'ordre ) account
        double montantTotalOperation = montantSaisi + 0.5 * fraisTransfert;
        // ? : this amount should be added to the ( benificary ) account
        double montantObtenu = montantSaisi -  0.5 *fraisTransfert;

    }
}
