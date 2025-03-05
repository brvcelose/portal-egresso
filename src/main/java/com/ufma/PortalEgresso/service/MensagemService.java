package com.ufma.PortalEgresso.service;

import com.ufma.PortalEgresso.exception.RegraNegocioRunTime;
import com.ufma.PortalEgresso.model.entity.ENUMs.Status;
import com.ufma.PortalEgresso.model.entity.Mensagem;
import com.ufma.PortalEgresso.model.repo.MensagemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Service
public class MensagemService {
    @Autowired
    private MensagemRepo repo;

    @Transactional
    public Mensagem salvar(Mensagem mensagem) {
        verificarMensagem(mensagem);

        if (mensagem.getDataEnvio() == null) {
            mensagem.setDataEnvio(LocalDateTime.now());
        }

        Mensagem salvo = repo.save(mensagem);

        return salvo;
    }

    @Transactional
    public void deletar(UUID id){
        verificarId(id);

        repo.deleteById(id);
    }

    @Transactional
    public List<Mensagem> listarMensagensOrdenadas(UUID idDiscussao) {
        return repo.findByGrupoIdOrderByDataEnvioAsc(idDiscussao);
    }

    private void verificarId(UUID id) {
        if (id == null)
            throw new RegraNegocioRunTime("ID inválido");
        if (!repo.existsById(id)){
            throw new RegraNegocioRunTime("ID não encontrado");
        }
    }

    private void verificarMensagem(Mensagem mensagem) {
        if (mensagem == null)
            throw new RegraNegocioRunTime("Mensagem inválida");

        if ((mensagem.getTexto() == null) || (mensagem.getTexto().trim().isEmpty()))
            throw new RegraNegocioRunTime("A mensagem deve estar preenchida");

        if ((mensagem.getEgresso() == null))
            throw new RegraNegocioRunTime("A mensagem deve estar associada a um egresso");
    }

}
