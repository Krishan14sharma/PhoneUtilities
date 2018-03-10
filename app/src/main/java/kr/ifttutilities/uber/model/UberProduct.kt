package kr.ifttutilities.uber.model
import com.google.gson.annotations.SerializedName



/**
 * Created by krishan on 09/03/18.
 */

data class ProductWrapper(
		@SerializedName("products") val products: List<Product>?
)

data class Product(
		@SerializedName("upfront_fare_enabled") val upfrontFareEnabled: Boolean,
		@SerializedName("capacity") val capacity: Int,
		@SerializedName("product_id") val productId: String,
		@SerializedName("price_details") val priceDetails: PriceDetails,
		@SerializedName("image") val image: String,
		@SerializedName("cash_enabled") val cashEnabled: Boolean,
		@SerializedName("shared") val shared: Boolean,
		@SerializedName("short_description") val shortDescription: String,
		@SerializedName("display_name") val displayName: String,
		@SerializedName("product_group") val productGroup: String,
		@SerializedName("description") val description: String
)

data class PriceDetails(
		@SerializedName("service_fees") val serviceFees: List<Any>,
		@SerializedName("cost_per_minute") val costPerMinute: Double,
		@SerializedName("distance_unit") val distanceUnit: String,
		@SerializedName("minimum") val minimum: Double,
		@SerializedName("cost_per_distance") val costPerDistance: Double,
		@SerializedName("base") val base: Double,
		@SerializedName("cancellation_fee") val cancellationFee: Double,
		@SerializedName("currency_code") val currencyCode: String
)