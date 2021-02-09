package dominando.android.hotel

import android.view.View
import android.widget.RatingBar
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

/**
 *
 *  Author:     Anthoni Ipiranga
 *  Project:    Tests
 *  Date:       09/02/2021
 */
class RatingBarMatcher(private val rating: Float) :
    BoundedMatcher<View, RatingBar>(RatingBar::class.java) {
    override fun describeTo(description: Description?) {
        description
            ?.appendText("With hint rating value: ")
            ?.appendValue(rating)
    }

    override fun matchesSafely(item: RatingBar): Boolean {
        return item.rating == rating
    }

    companion object {
        fun withRatingValue(rating: Float): RatingBarMatcher {
            return RatingBarMatcher(rating)
        }
    }
}