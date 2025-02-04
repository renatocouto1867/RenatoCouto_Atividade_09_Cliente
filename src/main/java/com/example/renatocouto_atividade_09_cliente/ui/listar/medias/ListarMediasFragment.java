package com.example.renatocouto_atividade_09_cliente.ui.listar.medias;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.renatocouto_atividade_09_cliente.databinding.FragmentListarMediasBinding;

import java.util.Locale;

public class ListarMediasFragment extends Fragment {
    private FragmentListarMediasBinding binding;
    private ItemListarMediasAdapter itemListarMediasAdapter;
    private TextToSpeech textToSpeech;
    private ListarMediasViewModel viewModel;

    public ListarMediasFragment() {
    }

    public static ListarMediasFragment newInstance() {
        return new ListarMediasFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListarMediasBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ListarMediasViewModel.class);

        configurarObservers();
        inicializarTextToSpeech();
        binding.buttonLer.setOnClickListener(v -> lerResumo());

        return binding.getRoot();
    }

    private void configurarObservers() {
        viewModel.getAlunosLiveData().observe(getViewLifecycleOwner(), alunos -> {
            itemListarMediasAdapter = new ItemListarMediasAdapter(alunos);
            binding.recyclerViewMedia.setAdapter(itemListarMediasAdapter);
            binding.recyclerViewMedia.setLayoutManager(new LinearLayoutManager(requireContext()));
        });

        viewModel.getIsCarregando().observe(getViewLifecycleOwner(), carregando -> {
            if (carregando) {
                binding.mediaProgressCircular.setVisibility(View.VISIBLE);
                binding.tvMediaCarregando.setVisibility(View.VISIBLE);
            } else {
                binding.mediaProgressCircular.setVisibility(View.GONE);
                binding.tvMediaCarregando.setVisibility(View.GONE);
            }
        });
    }

    private void inicializarTextToSpeech() {
        textToSpeech = new TextToSpeech(requireContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                Locale locale = new Locale("pt", "BR");
                int result = textToSpeech.setLanguage(locale);
                textToSpeech.setSpeechRate(1.2f);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TextToSpeech", "Idioma não suportado");
                }
            } else {
                Log.e("TextToSpeech", "Falha na inicialização");
            }
        });
    }

    private void lerResumo() {
        viewModel.getMensagemResumo().observe(getViewLifecycleOwner(), mensagem -> {
            if (textToSpeech != null && !textToSpeech.isSpeaking()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textToSpeech.speak(mensagem, TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    textToSpeech.speak(mensagem, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        binding = null;
    }
}
