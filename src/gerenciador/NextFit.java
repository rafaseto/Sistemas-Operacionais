package gerenciador;

import java.util.LinkedList;

public class NextFit {
    private int tamanhoUnidadeAlocacao;
    private int tamanhoMemoria;
    private int comprimentoMemoria;
    LinkedList<SegmentoMemoria> listaEncadeada;
    int ultimoIndiceAloc;    // posi��o que se encontra a �ltima aloc.

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
        // Objeto do tipo SegmentoMemoria que representa o seg. que ser� alocado para o processo
        SegmentoMemoria segmento = new SegmentoMemoria(true, 0, processo.getComprimento());

        processo.setSegmento(segmento);     // 'segmento' � atribu�do ao processo
        int comprimento = segmento.getComprimento();    // comprimento do segmento

        int indiceAloc = buscaSegmento(segmento.getComprimento());

        if (indiceAloc == -1) {
            System.out.println("N�o existe mem�ria suficiente para alocar este processo de comprimento " + processo.getComprimento());
            System.out.println("\n");
        } else {
            atualizaLista(indiceAloc, segmento);

            // Atualizando o �ltimo �ndice alocado
            ultimoIndiceAloc = indiceAloc;
        }
    }

    public int buscaSegmento(int comprimento) {
        // Buscando um segmento de mem�ria para a aloca��o a partir do ponto que t�nhamos parado
        for (int i = ultimoIndiceAloc + 1; i < listaEncadeada.size(); i++) {
            // Se o comprimento do seg. for >= ao tamanho exigido pelo processo, o seg. � escolhido
            if (
                    !listaEncadeada.get(i).isOcupado()
                            && listaEncadeada.get(i).getComprimento() >= comprimento
            ) {
                return i;
            }
        }

        // Buscando do in�cio caso n�o tenha encontrado depois do 'ultimoIndiceAloc'
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

    //Toda vez que um segmento � inserido na lista, o �ndice dos segmentos armazenados na lista precisa ser atualizada
    public void atualizaLista(int indice, SegmentoMemoria seg) {

        //pega o comprimento do segmento que ser� utilizado para alocar o processo
        int comprimento = listaEncadeada.get(indice).getComprimento();

        //se o comprimento do segmento � igual ao necess�rio para alocar o processo, nenhuma atualiza��o � necess�ria
        if (comprimento == seg.getComprimento()) {
            //atualiza a posi��o inicial do novo semento
            seg.setPosicaoInicial(listaEncadeada.get(indice).getPosicaoInicial());

            //insere novo segmento na lista encadeada
            listaEncadeada.set(indice, seg);

            //se o comprimento do segmento for maior, ser� necess�rio atualizar a posi��o inicial desse segmento e tamb�m o seu comprimento
        } else {
            //atualiza a posi��o inicial do novo semento
            seg.setPosicaoInicial(listaEncadeada.get(indice).getPosicaoInicial());

            //acrescenta o segmento novo
            listaEncadeada.add(indice, seg);
            //atualiza o segmento antigo, reduzindo o tamanho do comprimento dele e atualizando a posi��o inicial
            SegmentoMemoria segAntigo = listaEncadeada.get(indice + 1);

            segAntigo.setComprimento(segAntigo.getComprimento() - seg.getComprimento());
            segAntigo.setPosicaoInicial(seg.getPosicaoInicial() + seg.getComprimento());

            //atualiza a posi��o inicial dos demais segmentos existentes na lista. Se n�o existir mais segmentos, nada ser� feito.
            SegmentoMemoria aux;

            for (int a = indice + 2; a < listaEncadeada.size(); a++) {

                aux = listaEncadeada.get(a);

                aux.setPosicaoInicial(listaEncadeada.get(a - 1).getPosicaoInicial() + listaEncadeada.get(a - 1).getComprimento());
            }
        }
    }

    public void exibeSegmentosMemoria() {

        System.out.println("-------- Segmentos de Mem�ria -------- \n");

        for (int a = 0; a < listaEncadeada.size(); a++) {
            System.out.println("Segmento [" + a + "] --> " + listaEncadeada.get(a));
        }

        System.out.println("\n\n");
    }

    public void exibeListaEncadeada() {

        System.out.println("-------- Segmentos de Mem�ria -------- \n");

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
        //Gerenciador configurado para uma mem�ria de 62KB e dividida em unidades de aloca��o de 2KB
        NextFit gerenciador = new NextFit(2, 62);

        //cria um processo que precisa de um segmento de mem�ria com um comprimento maior ou igual a 10 para ser executado
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
