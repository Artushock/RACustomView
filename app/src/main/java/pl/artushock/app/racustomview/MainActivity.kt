package pl.artushock.app.racustomview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import pl.artushock.app.racustomview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.bottomButton.setOnClickListener {
            when (it){
                BottomButtonAction.POSITIVE -> {
                    binding.bottomButton.setPositiveButtonText("Positive button")
                }
                BottomButtonAction.NEGATIVE -> {
                    binding.bottomButton.setNegativeButtonText("Negative button")
                }
            }
        }
    }
}