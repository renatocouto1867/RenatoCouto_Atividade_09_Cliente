package com.example.renatocouto_atividade_09_cliente.entity;



import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "alunos")
public class Aluno implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String nome;
    private double nota1;
    private double nota2;
    private  String situacao;

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public void setNota1(double nota1) {
        this.nota1 = nota1;
    }

    public void setNota2(double nota2) {
        this.nota2 = nota2;
    }

    public Aluno(){};


    public Aluno(Long id, String nome, double nota1, double nota2) {
        this.id = id;
        this.nome = nome;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.situacao = calcularSituacao(nota1, nota2);
    }

    public Aluno(String nome, double nota1, double nota2) {
        this.nome = nome;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.situacao = calcularSituacao(nota1, nota2);
    }

    public void atualizaNotas(double nota1, double nota2) {
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.situacao = calcularSituacao(nota1, nota2);
    }

    public void atualizarNomeNota(String  nome, double nota1, double nota2){
        this.nome = nome;
        atualizaNotas(nota1, nota2);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    private String calcularSituacao(double nota1, double nota2) {
        return (getMedia() >= 6.0) ? "Aprovado" : "Reprovado";
    }

    public double getMedia() {
        return (nota1 + nota2) / 2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getSituacao() {
        return situacao;
    }

    public double getNota1() {
        return nota1;
    }

    public double getNota2() {
        return nota2;
    }
}