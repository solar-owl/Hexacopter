package com.example.hexacopterapp

import net.schmizz.sshj.transport.verification.HostKeyVerifier
import java.security.PublicKey

class NullHostKeyVerifier : HostKeyVerifier {
    override fun verify(arg0: String?, arg1: Int, arg2: PublicKey?): Boolean {
        return true
    }

//    override fun findExistingAlgorithms(hostname: String?, port: Int): MutableList<String> {
//    }
}