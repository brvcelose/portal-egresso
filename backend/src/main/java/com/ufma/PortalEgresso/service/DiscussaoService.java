package com.ufma.PortalEgresso.service;

import com.ufma.PortalEgresso.exception.BuscaVaziaRunTime;
import com.ufma.PortalEgresso.exception.RegraNegocioRunTime;
import com.ufma.PortalEgresso.model.entity.Discussao;
import com.ufma.PortalEgresso.model.repo.DiscussaoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DiscussaoService {

    @Autowired
    private DiscussaoRepo repo;

    @Transactional
    public Discussao salvar(Discussao discussao) {
        verificarDiscussao(discussao);
        Discussao salvo = repo.save(discussao);

        return salvo;
    }

    @Transactional
    public Discussao atualizar(Discussao discussao) {
        verificarDiscussao(discussao);
        verificarId(discussao.getId_discussao());

        return repo.save(discussao);
    }

    public Optional<Discussao> buscarPorId(UUID id) {
        verificarId(id);

        return repo.findById(id);
    }

    public List<Discussao> listarTodos() {
        List<Discussao> lista = repo.findAll();

        if (lista.isEmpty()){
            throw new BuscaVaziaRunTime();
        }

        return lista;
    }

    @Transactional
    public void deletar(UUID id){
        verificarId(id);
        repo.deleteById(id);
    }

    private void verificarId(UUID id) {
        if (id == null)
            throw new RegraNegocioRunTime("ID inválido");
        if (!repo.existsById(id)){
            throw new RegraNegocioRunTime("ID não encontrado");
        }
    }

    private void verificarDiscussao(Discussao discussao) {
        if (discussao == null)
            throw new RegraNegocioRunTime("Discussão inválida");

        if ((discussao.getTitulo() == null) || (discussao.getTitulo().trim().isEmpty()))
            throw new RegraNegocioRunTime("A discussão deve possuir um título");

        if (discussao.getEgresso() == null)
            throw new RegraNegocioRunTime("A discussão deve estar associada a um egresso");
    }

}
