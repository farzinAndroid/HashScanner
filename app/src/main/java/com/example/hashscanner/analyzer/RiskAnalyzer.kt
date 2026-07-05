package com.example.hashscanner.analyzer

import android.Manifest
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo

data class RiskResult(

    val score:Int,

    val level:String,

    val reasons:String

)

class RiskAnalyzer {

    fun analyze(

        pkg: PackageInfo,

        app: ApplicationInfo,

        installer:String

    ): RiskResult {

        var score = 0

        val reasons = mutableListOf<String>()

        if ((app.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0) {

            score += 15

            reasons.add("Debuggable App")

        }

        if ((app.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {

            score += 5

        }

        if (
            installer == "Unknown"
        ) {

            score += 20

            reasons.add("Unknown Installer")

        }

        pkg.requestedPermissions?.forEach {

            when(it){

                Manifest.permission.SYSTEM_ALERT_WINDOW ->{

                    score+=20

                    reasons.add("Overlay Permission")

                }

                Manifest.permission.REQUEST_INSTALL_PACKAGES ->{

                    score+=20

                    reasons.add("Can Install APK")

                }

                Manifest.permission.BIND_ACCESSIBILITY_SERVICE ->{

                    score+=25

                    reasons.add("Accessibility Service")

                }

                Manifest.permission.RECORD_AUDIO ->{

                    score+=5

                    reasons.add("Microphone")

                }

                Manifest.permission.CAMERA ->{

                    score+=5

                    reasons.add("Camera")

                }

                Manifest.permission.READ_SMS ->{

                    score+=15

                    reasons.add("Read SMS")

                }

                Manifest.permission.SEND_SMS ->{

                    score+=15

                    reasons.add("Send SMS")

                }

                Manifest.permission.RECEIVE_BOOT_COMPLETED ->{

                    score+=10

                    reasons.add("Auto Start")

                }

            }

        }

        if(score>100)
            score=100

        val level=

            when{

                score>=80->"CRITICAL"

                score>=60->"HIGH"

                score>=40->"MEDIUM"

                score>=20->"LOW"

                else->"SAFE"

            }

        return RiskResult(

            score,

            level,

            reasons.joinToString(",")

        )

    }

}