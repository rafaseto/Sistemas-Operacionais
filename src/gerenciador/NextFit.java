package gerenciador;

import java.util.LinkedList;

public class NextFit {
    private int tamanhoUnidadeAlocacao;
    private int tamanhoMemoria;
    private int comprimentoMemoria;
    LinkedList<SegmentoMemoria> listaEncadeada;
    int ultimoIndiceAloc;    // posição que se encontra a última aloc.

    public NextFit(int tamanhoUnidadeAlocacao, int tamanhoMemoria) {
        this.tamanhoUnidadeAlocacao = tamanhoUnidadeAlocacao;
        this.tamanhoMemoria = tamanhoMemoria;
        this.comprimentoMemoria = tamanhoMemoria / tamanhoUnidadeAlocacao;
        this.listaEncadeada = new LinkedList<>();
        this.ultimoIndiceAloc = -1;

        // Criando o primeiro seg. de mem.
        listaEncadeada.add(new SegmentoMemoria(false, 0, comprimentoMemoria));
    }

    // Retorna o tamanho da unidade de aloc.
    public int getTamanhoUnidadeAlocacao() {
        return tamanhoUnidadeAlocacao;
    }

    // Define o tamanho da unidade de aloc.
    public void setTamanhoUnidadeAlocacao(int tamanhoUnidadeAlocacao) {
        this.tamanhoUnidadeAlocacao = tamanhoUnidadeAlocacao;
    }

    // Retorna o tamanho da mem.
    public int getTamanhoMemoria() {
        return tamanhoMemoria;
    }

    // Define o tamanho da mem.
    public void setTamanhoMemoria(int tamanhoMemoria) {
        this.tamanhoMemoria = tamanhoMemoria;
    }

    public void alocaProcesso(Processo processo) {
        // Objeto do tipo SegmentoMemoria que representa o seg. que será alocado para o processo
        SegmentoMemoria segmento = new SegmentoMemoria(true, 0, processo.getComprimento());

        processo.setSegmento(segmento);     // 'segmento' é atribuído ao processo
        int comprimento = segmento.getComprimento();    // comprimento do segmento

        int indiceAloc = buscaSegmento(segmento.getComprimento());

        if (indiceAloc == -1) {
            System.out.println("Não existe memória suficiente para alocar este processo de comprimento " + processo.getComprimento());
            System.out.println("\n");
        } else {
            atualizaLista(indiceAloc, segmento);

            // Atualizando o último índice alocado
            ultimoIndiceAloc = indiceAloc;
        }
    }

    public int buscaSegmento(int comprimento) {
        // Buscando um segmento de memória para a alocação a partir do ponto que tínhamos parado
        for (int i = ultimoIndiceAloc + 1; i < listaEncadeada.size(); i++) {
            // Se o comprimento do seg. for >= ao tamanho exigido pelo processo, o seg. é escolhido
            if (
                    !listaEncadeada.get(i).isOcupado()
                            && listaEncadeada.get(i).getComprimento() >= comprimento
            ) {
                return i;
            }
        }

        // Buscando do início caso não tenha encontrado depois do 'ultimoIndiceAloc'
        for (int i = 0; i <= ultimoIndiceAloc; i++ ) {
            if (
                    !listaEncadeada.get(i).isOcupado()
                            && listaEncadeada.get(i).getComprimento() >= comprimento
            ) {
                return i;
            }
        }
        return -1;
    }

    //Toda vez que um segmento é inserido na lista, o índice dos segmentos armazenados na lista precisa ser atualizada
    public void atualizaLista(int indice, SegmentoMemoria seg) {

        //pega o comprimento do segmento que será utilizado para alocar o processo
        int comprimento = listaEncadeada.get(indice).getComprimento();

        //se o comprimento do segmento é igual ao necessário para alocar o processo, nenhuma atualização é necessária
        if (comprimento == seg.getComprimento()) {
            //atualiza a posição inicial do novo semento
            seg.setPosicaoInicial(listaEncadeada.get(indice).getPosicaoInicial());

            //insere novo segmento na lista encadeada
            listaEncadeada.set(indice, seg);

            //se o comprimento do segmento for maior, será necessário atualizar a posição inicial desse segmento e também o seu comprimento
        } else {
            //atualiza a posição inicial do novo semento
            seg.setPosicaoInicial(listaEncadeada.get(indice).getPosicaoInicial());

            //acrescenta o segmento novo
            listaEncadeada.add(indice, seg);
            //atualiza o segmento antigo, reduzindo o tamanho do comprimento dele e atualizando a posição inicial
            SegmentoMemoria segAntigo = listaEncadeada.get(indice + 1);

            segAntigo.setComprimento(segAntigo.getComprimento() - seg.getComprimento());
            segAntigo.setPosicaoInicial(seg.getPosicaoInicial() + seg.getComprimento());

            //atualiza a posição inicial dos demais segmentos existentes na lista. Se não existir mais segmentos, nada será feito.
            SegmentoMemoria aux;

            for (int a = indice + 2; a < listaEncadeada.size(); a++) {

                aux = listaEncadeada.get(a);

                aux.setPosicaoInicial(listaEncadeada.get(a - 1).getPosicaoInicial() + listaEncadeada.get(a - 1).getComprimento());
            }
        }
    }

    public void exibeSegmentosMemoria() {

        System.out.println("-------- Segmentos de Memória -------- \n");

        for (int a = 0; a < listaEncadeada.size(); a++) {
            System.out.println("Segmento [" + a + "] --> " + listaEncadeada.get(a));
        }

        System.out.println("\n\n");
    }

    public void exibeListaEncadeada() {

        System.out.println("-------- Segmentos de Memória -------- \n");

        for (int a = 0; a < listaEncadeada.size(); a++) {
            if (listaEncadeada.size() - 1 == a) {
                System.out.print(listaEncadeada.get(a));
            } else {
                System.out.print(listaEncadeada.get(a) + " --> ");
            }
        }

        System.out.println("\n\n");
    }

    public static void main(String[] args) {
        //Gerenciador configurado para uma memória de 62KB e dividida em unidades de alocação de 2KB
        NextFit gerenciador = new NextFit(2, 62);

        //cria um processo que precisa de um segmento de memória com um comprimento maior ou igual a 10 para ser executado
        Processo d = new Processo(10);

        Processo e = new Processo(3);
        Processo f = new Processo(1);
        Processo g = new Processo(10);
        Processo h = new Processo(6);
        Processo i = new Processo(5);

        gerenciador.exibeListaEncadeada();

        gerenciador.alocaProcesso(d);

        gerenciador.exibeListaEncadeada();

        gerenciador.alocaProcesso(e);

        gerenciador.exibeListaEncadeada();

        gerenciador.alocaProcesso(f);

        gerenciador.exibeListaEncadeada();

        //Processo finalizado
        e.setOnExecution(false);

        gerenciador.exibeListaEncadeada();

        gerenciador.alocaProcesso(f);

        gerenciador.exibeListaEncadeada();

        d.setOnExecution(false);

        gerenciador.exibeListaEncadeada();

        gerenciador.alocaProcesso(g);

        gerenciador.exibeListaEncadeada();

        gerenciador.alocaProcesso(h);

        gerenciador.exibeListaEncadeada();

        gerenciador.alocaProcesso(i);

        gerenciador.exibeListaEncadeada();
    }
}
