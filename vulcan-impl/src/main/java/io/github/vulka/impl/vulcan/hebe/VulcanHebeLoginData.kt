package io.github.vulka.impl.vulcan.hebe

import io.github.vulka.core.api.LoginData
import io.github.vulka.impl.vulcan.hebe.login.HebeKeystore

class VulcanHebeLoginData(
    val symbol: String,
    val token: String,
    val pin: String,
    val keystore: HebeKeystore
) : LoginData()