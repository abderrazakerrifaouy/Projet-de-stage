package com.example.projet_de_stage.fragment




import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.projet_de_stage.R

class BirthDateFragment : Fragment() {
    private lateinit var nextButton : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.birth_date, container, false)

        nextButton = view.findViewById(R.id.nextButton)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nextButton.setOnClickListener {
            val phoneNumberFragment = PhoneNumberFragment()
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,  // حركة الدخول
                    R.anim.fade_out,  // حركة الخروج
                    R.anim.fade_in,   // حركة الدخول عند الرجوع
                    R.anim.slide_out  // حركة الخروج عند الرجوع
                )
                .replace(R.id.fragment_container, phoneNumberFragment)
                .addToBackStack("BirthDateFragment")
                .commit()
        }

    }
}

//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//
//
//class BirthDateFragment : Fragment() {
//    private var _binding: FragmentBirthDateBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var datePicker: MaterialDatePicker<Long>
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentBirthDateBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        setupDatePicker()
//
//        binding.birthdateCard.setOnClickListener {
//            datePicker.show(parentFragmentManager, "DATE_PICKER")
//        }
//
//        binding.nextButton.setOnClickListener {
//            findNavController().navigate(R.id.action_birthDateFragment_to_mainFragment)
//        }
//    }
//
//    private fun setupDatePicker() {
//        val constraintsBuilder = CalendarConstraints.Builder()
//            .setValidator(DateValidatorPointBackward.now())
//
//        datePicker = MaterialDatePicker.Builder.datePicker()
//            .setTitleText(getString(R.string.select_birthdate))
//            .setCalendarConstraints(constraintsBuilder.build())
//            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
//            .build()
//
//        datePicker.addOnPositiveButtonClickListener { selectedDate ->
//            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
//            calendar.timeInMillis = selectedDate
//
//            val age = calculateAge(calendar.get(Calendar.YEAR))
//            val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ar"))
//            binding.birthdateDisplay.text =
//                "${dateFormat.format(calendar.time)} ($age ${getString(R.string.years)})"
//        }
//    }
//
//    private fun calculateAge(birthYear: Int): Int {
//        return Calendar.getInstance().get(Calendar.YEAR) - birthYear
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}