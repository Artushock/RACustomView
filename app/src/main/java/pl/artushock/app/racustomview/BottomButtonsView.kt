package pl.artushock.app.racustomview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import pl.artushock.app.racustomview.databinding.PartButtonsBinding

enum class BottomButtonAction {
    POSITIVE, NEGATIVE
}

typealias OnBottomButtonClickListener = (BottomButtonAction) -> Unit

class BottomButtonsView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int,
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {
    private val binding: PartButtonsBinding
    private var listener: OnBottomButtonClickListener? = null

    var isProgressMode: Boolean = false
        set(value) {
            field = value
            if (value) {
                binding.positiveButton.visibility = GONE
                binding.negativeButton.visibility = GONE
                binding.progress.visibility = VISIBLE
            } else {
                binding.positiveButton.visibility = VISIBLE
                binding.negativeButton.visibility = VISIBLE
                binding.progress.visibility = GONE
            }
        }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context,
        attrs,
        defStyleAttr,
        0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        val layoutInflater = LayoutInflater.from(context)
        layoutInflater.inflate(R.layout.part_buttons, this, true)
        binding = PartButtonsBinding.bind(this)
        initializeAttributes(attrs, defStyleAttr, defStyleRes)
        initListeners()
    }

    private fun initializeAttributes(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return

        val typeArray = context.obtainStyledAttributes(attrs,
            R.styleable.BottomButtonsView,
            defStyleAttr,
            defStyleRes)

        with(binding) {
            val positiveText =
                typeArray.getText(R.styleable.BottomButtonsView_bottomButtonPositiveButtonText)
            setPositiveButtonText(positiveText.toString())

            val negativeText =
                typeArray.getText(R.styleable.BottomButtonsView_bottomButtonNegativeButtonText)
            setNegativeButtonText(negativeText.toString())

            val positiveButtonColor =
                typeArray.getColor(R.styleable.BottomButtonsView_bottomButtonPositiveButtonBackground,
                    Color.WHITE)
            positiveButton.backgroundTintList = ColorStateList.valueOf(positiveButtonColor)

            val negativeButtonColor =
                typeArray.getColor(R.styleable.BottomButtonsView_bottomButtonNegativeButtonBackground,
                    Color.BLACK)
            negativeButton.backgroundTintList = ColorStateList.valueOf(negativeButtonColor)

            isProgressMode =
                typeArray.getBoolean(R.styleable.BottomButtonsView_bottomButtonProgressMode, false)
        }
        typeArray.recycle()
    }

    private fun initListeners() {
        binding.positiveButton.setOnClickListener {
            listener?.invoke(BottomButtonAction.POSITIVE)
        }

        binding.negativeButton.setOnClickListener {
            listener?.invoke(BottomButtonAction.NEGATIVE)
        }
    }

    fun setOnClickListener(listener: OnBottomButtonClickListener) {
        this.listener = listener
    }

    fun setPositiveButtonText(s: String) {
        binding.positiveButton.text = s
    }

    fun setNegativeButtonText(s: String) {
        binding.negativeButton.text = s
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()!!
        val savedState = SavedState(superState)
        savedState.positiveButtonText = binding.positiveButton.text.toString()
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        binding.positiveButton.text = savedState.positiveButtonText
    }

    class SavedState : BaseSavedState {

        var positiveButtonText: String? = null

        constructor(superState: Parcelable) : super(superState)
        constructor(parcel: Parcel) : super(parcel) {
            positiveButtonText = parcel.readString()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(positiveButtonText)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(parcel: Parcel): SavedState {
                    return SavedState(parcel)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return Array(size) { null }
                }
            }
        }
    }

}