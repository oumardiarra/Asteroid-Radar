package com.udacity.asteroidradar

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidListAdapter
import timber.log.Timber


@BindingAdapter("listData")
fun bindRecycleView(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidListAdapter
    adapter.submitList(data)
}

@BindingAdapter("progressStaus")
fun bindAsteroidFetchingData(view: View, it: Any?) {
    view.visibility = if (it == null) View.VISIBLE else View.GONE
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }

}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription =
            context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
    textView.contentDescription =
        "${context.getString(R.string.astronomical_unit_format)} ${String.format(context.getString(R.string.astronomical_unit_format), number)}"
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
    textView.contentDescription = "${context.getString(R.string.estimated_diameter_title)} ${String.format(context.getString(R.string.km_unit_format), number)}"

}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
    textView.contentDescription = "${context.getString(R.string.relative_velocity_title)} ${String.format(context.getString(R.string.km_unit_format), number)}"
}

@SuppressLint("StringFormatInvalid")
@BindingAdapter("picOfDay")

fun bindImageViewPicOfDay(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    val context = imageView.context
    val imgUri = pictureOfDay?.url?.toUri()?.buildUpon()?.scheme("https")?.build()
    Picasso.with(context).load(imgUri).into(imageView)
    imageView.contentDescription =
        "${context.getString(R.string.image_of_the_day)} ${pictureOfDay?.title}"


}



