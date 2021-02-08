package dominando.android.hotel

import dominando.android.hotel.form.HotelValidator
import dominando.android.hotel.model.Hotel
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 *
 *  Author:     Anthoni Ipiranga
 *  Project:    Tests
 *  Date:       08/02/2021
 */

class HotelValidatorTests {

    private val validator by lazy {
        HotelValidator()
    }
    private val validInfo = Hotel(
        name = "Ritz Lago da Anta",
        address = "Ab. Brigadeiro Eduardo Gomes de Brito, 546, Maceió/AL",
        rating = 4.9f
    )

    @Test
    fun `should validate info for a valid hotel`() {
        //Junit4 Style
        assertTrue(validator.validate(validInfo))
        //AssertJ Style
        assertThat(validator.validate(validInfo)).isTrue()
    }

    @Test
    fun `should not valitate info without a hotel name`() {
        val missingName = validInfo.copy(name = "")
        assertThat(validator.validate(missingName)).isFalse()
    }

    @Test
    fun `should not validate info without a hotel address`() {
        val missingAddress = validInfo.copy(address = "")
        assertThat(validator.validate(missingAddress)).isFalse()
    }

    @Test
    fun `should not validate info when name is outside accepted size`() {
        val nameTooShort = validInfo.copy(name = "I")
        assertThat(validator.validate(nameTooShort)).isFalse()
    }

    @Test
    fun `should not validate info when address is outside accepted size`() {
        val bigAddress =
            "Av. Brigadeiro Eduardo Gomes Brito, 546 -" +
                    "Lago da Anta, Maceió - AL, 57038-230" +
                    " Casa grande esquina com a quadra de basket e a prefeitura munipal"
        val addressToLong = validInfo.copy(address = bigAddress)
        assertThat(validator.validate(addressToLong)).isFalse()
    }
}
