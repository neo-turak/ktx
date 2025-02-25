package com.github.neoturak

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.github.neoturak.common.registerLauncher
import com.github.neoturak.common.singleClick
import com.github.neoturak.common.startActivityWithResult
import com.github.neoturak.databinding.FragmentMainBinding

/**
 * @author 努尔江
 * Created on: 2023/2/9
 * @project ktx
 * Description:
 **/

class MainFragment : Fragment(){
    private lateinit var binding:FragmentMainBinding
    private lateinit var launcher:ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launcher = registerLauncher {
            binding.tvName.text = it?.getStringExtra("hola")
        }
        binding.btnGo.singleClick {
            startActivityWithResult<AnkoActivity>(launcher)
        }
    }
}