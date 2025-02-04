package com.example.renatocouto_atividade_09_cliente.ui.listar.medias;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.renatocouto_atividade_09_cliente.R;
import com.example.renatocouto_atividade_09_cliente.entity.Aluno;

import java.util.ArrayList;
import java.util.List;

public class ListarMediasViewModel extends AndroidViewModel {
    private static final Uri URI_ALUNOS = Uri.parse(
            "content://com.example.renatocouto_atividade_09_provider/alunos");

    private final MutableLiveData<List<Aluno>> alunosLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isCarregando = new MutableLiveData<>(true);
    private final MutableLiveData<String> mensagemResumo = new MutableLiveData<>();
    private final Context context;

    /**
     * https://stackoverflow.com/questions/51451819/how-to-get-context-in-android-mvvm-viewmodel
     * para pegar o contexto da aplicação
     */
    public ListarMediasViewModel(Application application) {
        super(application);
        this.context = application.getApplicationContext();
        carregarAlunosMedias(context);
    }

    public LiveData<List<Aluno>> getAlunosLiveData() {
        return alunosLiveData;
    }

    public LiveData<Boolean> getIsCarregando() {
        return isCarregando;
    }

    public LiveData<String> getMensagemResumo() {
        return mensagemResumo;
    }

    public void carregarAlunosMedias(Context context) {
        new Thread(() -> {
            List<Aluno> alunoList = new ArrayList<>();
            StringBuilder aprovados = new StringBuilder();
            StringBuilder reprovados = new StringBuilder();

            ContentResolver contentResolver = getApplication().getContentResolver();
            Cursor cursor = contentResolver.query(URI_ALUNOS, null, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Aluno aluno = new Aluno();
                    aluno.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
                    aluno.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                    aluno.setNota1(cursor.getDouble(cursor.getColumnIndexOrThrow("nota1")));
                    aluno.setNota2(cursor.getDouble(cursor.getColumnIndexOrThrow("nota2")));
                    aluno.setSituacao(cursor.getString(cursor.getColumnIndexOrThrow("situacao")));

                    if (aluno.getSituacao().equals("Aprovado")) {
                        aprovados.append(aluno.getNome()).append(", ");
                    } else {
                        reprovados.append(aluno.getNome()).append(", ");
                    }

                    alunoList.add(aluno);
                }
                cursor.close();
            }

            StringBuilder msg = new StringBuilder();
            if (aprovados.length() > 0) {
                msg.append(context.getString(R.string.os_alunos_aprovados_foram)).append(aprovados);
            }
            if (reprovados.length() > 0) {
                msg.append(context.getString(R.string.os_alunos_reprovados_foram)).append(reprovados);
            }
            msg.append(".");

            alunosLiveData.postValue(alunoList);
            mensagemResumo.postValue(msg.toString());
            isCarregando.postValue(false);

        }).start();
    }
}
