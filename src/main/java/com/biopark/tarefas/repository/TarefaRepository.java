package com.biopark.tarefas.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.biopark.tarefas.model.Tarefa;
import com.biopark.tarefas.model.TarefaStatus;

@Repository
public class TarefaRepository {

    private final Map<Long, Tarefa> tarefas = new LinkedHashMap<>();
    private Long nextId = 1L;

    public TarefaRepository() {
        // Pré-cadastrar 3 tarefas de exemplo
        Tarefa t1 = new Tarefa("Estudar Spring Boot", "Revisar os conceitos de Spring Boot e Thymeleaf", TarefaStatus.PENDING);
        save(t1);

        Tarefa t2 = new Tarefa("Fazer compras", "Comprar frutas, legumes e pão no mercado", TarefaStatus.PENDING);
        save(t2);

        Tarefa t3 = new Tarefa("Organizar escritório", "Limpar a mesa e organizar os documentos", TarefaStatus.PENDING);
        save(t3);

        Tarefa t4 = new Tarefa("Organizar escritório", "Limpar a mesa e organizar os documentos", TarefaStatus.FINISHED);
        save(t4);
    }

    public Tarefa save(Tarefa tarefa) {
        if (tarefa.getId() == null) {
            tarefa.setId(nextId++);
        }
        tarefas.put(tarefa.getId(), tarefa);
        return tarefa;
    }

    public List<Tarefa> findAll() {
        return new ArrayList<>(tarefas.values());
    }

    public Optional<Tarefa> findById(Long id) {
        return Optional.ofNullable(tarefas.get(id));
    }

    public List<Tarefa> findByStatus(TarefaStatus status) {
        List<Tarefa> list = new ArrayList<>(tarefas.values());
        List<Tarefa> finalList = new ArrayList<>();

        for (Tarefa tarefa : list) {
            if (tarefa.getStatus() == status) {
                finalList.add(tarefa);
            }
        }

        return finalList;
    }

    public void deleteById(Long id) {
        tarefas.remove(id);
    }
}
