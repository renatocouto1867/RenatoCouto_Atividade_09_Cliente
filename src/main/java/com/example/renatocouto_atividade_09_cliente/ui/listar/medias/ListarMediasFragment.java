package com.example.renatocouto_atividade_09_cliente.ui.listar.medias;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.renatocouto_atividade_09_cliente.R;
import com.example.renatocouto_atividade_09_cliente.entity.Aluno;

import java.util.ArrayList;
import java.util.List;

public class ListarMediasFragment extends Fragment {

    public static final Uri URI_ALUNOS = Uri.parse(
            "content://com.example.renatocouto_atividade_09_provider/alunos");
    private ItemListarMediasAdapter itemListarMediasAdapter;
    private RecyclerView recyclerViewAlunoMedia;
    private ProgressBar progressBar;
    private TextView textViewProgress;

    public ListarMediasFragment() {

    }

    public static ListarMediasFragment newInstance() {
        return new ListarMediasFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listar_medias, container, false);

        inicializarViews(view);
        carregarAlunosMedias();

        return view;
    }

    private void inicializarViews(View view) {
        progressBar = view.findViewById(R.id.media_progress_circular);
        recyclerViewAlunoMedia = view.findViewById(R.id.recyclerViewMedia);
        textViewProgress = view.findViewById(R.id.tv_media_carregando);
    }

    private void carregarAlunosMedias() {
        ContentResolver contentResolver = requireContext().getContentResolver();
        Cursor cursor = contentResolver.query(URI_ALUNOS, null, null, null, null);

        exibirProgresso(true);
        List<Aluno> alunoList = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Aluno aluno = new Aluno();
                aluno.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
                aluno.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                aluno.setNota1(cursor.getDouble(cursor.getColumnIndexOrThrow("nota1")));
                aluno.setNota2(cursor.getDouble(cursor.getColumnIndexOrThrow("nota2")));
                aluno.setSituacao(cursor.getString(cursor.getColumnIndexOrThrow("situacao")));

                alunoList.add(aluno);
            }
            cursor.close();
        }

        configurarRecyclerView(alunoList);
        atualizarProgresso(alunoList);
    }

    private void atualizarProgresso(List<Aluno> alunoList) {
        if (alunoList != null && !alunoList.isEmpty()) {
            exibirProgresso(false);
        } else {
            progressBar.setVisibility(View.GONE);
            textViewProgress.setVisibility(View.VISIBLE);
            textViewProgress.setText(R.string.sem_aluno_cadastrado);
        }
    }

    private void exibirProgresso(boolean exibir) {
        progressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
        textViewProgress.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    private void configurarRecyclerView(List<Aluno> alunoList) {

        itemListarMediasAdapter = new ItemListarMediasAdapter(alunoList);

        recyclerViewAlunoMedia.setAdapter(itemListarMediasAdapter);
        recyclerViewAlunoMedia.setLayoutManager(new LinearLayoutManager(requireContext()));

    }


}
