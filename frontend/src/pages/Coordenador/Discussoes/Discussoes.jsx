import React, {useEffect, useState} from 'react';
import './Discussoes.css'
import DiscussaoCard from "../../../components/Coordenador/DiscussaoCard.jsx";
import Pagination from "../../../components/common/Pagination/Pagination.jsx";
import api from "../../../services/api.jsx";
import {useAuth} from "../../../contexts/AuthContext.jsx";

const Discussoes = () => {

    const { user } = useAuth();
    const [discussoes, setDiscussoes] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage] = useState(5); // Itens por página
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchDiscussoes = async () => {
            try {
                setLoading(true);
                const response = await api.get(`/api/discussao/listarTodos`);

                const discussoesFiltradas = response.data.filter(discussao =>
                    discussao.egresso?.cursos?.some(curso =>
                        curso.curso?.coordenador?.id_coordenador === user.id
                    )
                );

                setDiscussoes(discussoesFiltradas);
            } catch (error) {
                console.error("Erro ao buscar discussões:", error);
            } finally {
                setLoading(false);
            }

        };

        fetchDiscussoes();
    }, []);

    const totalPages = Math.ceil(discussoes.length / itemsPerPage);
    const paginatedDiscussoes = discussoes.slice(
        (currentPage - 1) * itemsPerPage,
        currentPage * itemsPerPage
    );

    const handlePageChange = (newPage) => {
        setCurrentPage(newPage);
        window.scrollTo(0, 0); //rolar para o topo
    };

    return (
        <div className="main__container">
            <section>
                <h1 className="line-text">Gerenciar Grupos de Discussão</h1>

                <div className="discussao-container">

                    {loading ? (
                        <div className="chart-skeleton" style={{marginTop: '1rem', height: '1.5rem', width: '80%'}}>
                            <div className="skeleton-loader"></div>
                        </div>
                    ) : (
                        <></>
                    )}

                    <div className="cards-discussoes">
                        {paginatedDiscussoes?.map((discussao) => (
                                <DiscussaoCard
                                    titulo={discussao.titulo}
                                    id={discussao?.id_discussao}
                                />
                            ))}
                    </div>

                    <div className="pagination-container">
                        <Pagination
                            currentPage={currentPage}
                            totalPages={totalPages}
                            onPageChange={handlePageChange}
                        />
                    </div>

                </div>
            </section>
        </div>
    );
};

export default Discussoes;