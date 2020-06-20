package parser;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import bitstructures.BitMatrix;
import bitstructures.BitVector;
import bitstructures.GeneralBitMatrixImpl;
import bitstructures.ImplementedMatrix;
import bitstructures.UpperTriangularBitMatrixImpl;
import representation.NetElement;
import representation.Place;
import representation.Transition;
import representation.Translator;

class PNMLHandler extends DefaultHandler {
    protected Translator translator;
    protected ImplementedMatrix F;
    private String text;
    private NetElement current;
    private boolean isText;
    private boolean isProbability;
    private String source, target;
    private double probability;
    private boolean isMarking;
    private Integer tokens;

    public PNMLHandler() {
        probability = 1;
        translator = new Translator();
        F = null;
        text = null;
        current = null;
        isText = false;
        isProbability = false;
        source = target = null;
    }

    public void addIntoF() {
        int i = translator.get(source);
        int j = translator.get(target);
        F.set(i, j, true);
        translator.get(j).addProbability(probability);
        target = source = null;
        probability = 1;
    }

    public void startElement(String uri, String localName,
                             String qName, Attributes atts) {
        if ("place".equals(qName)) {
            current = new Place(atts.getValue("id"), "", false);
        } else if ("transition".equals(qName)) {
            current = new Transition(atts.getValue("id"), "", false);
        } else if ("initialMarking".equals(qName)) {
            isMarking = true;

            isText = true;
        } else if ("text".equals(qName)) {
            isText = true;
        } else if ("arc".equals(qName)) {
            if (F == null)
                F = new GeneralBitMatrixImpl(translator.size());
            source = atts.getValue("source");
            target = atts.getValue("target");
        } else if ("probability".equals(qName)) {
            isProbability = true;
        }
    }

    public void endElement(String uri, String localName,
                           String qName) {
        if ("place".equals(qName)) {
            if (tokens != null)
                current.setTokens(tokens);
            tokens = null;

            translator.add(current);
            current = null;
        } else if ("transition".equals(qName)) {
            translator.add(current);
            current = null;
        } else if ("name".equals(qName)) {
            if (current != null)
                current.setName(text);
        } else if ("initialMarking".equals(qName)) {
            isMarking = false;
        } else if ("text".equals(qName)) {
            isText = false;
        } else if ("probability".equals(qName)) {
            isProbability = false;
        } else if ("arc".equals(qName)) {
            try {
                addIntoF();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void characters(char[] cbuf, int start, int len) {
        if (isText)
            text = new String(cbuf, start, len);
        if (isMarking) {
            try {
                tokens = Integer.parseInt(text);
            }catch(NumberFormatException ex){}
        } else if (isProbability) {
            try {
                probability = Double.parseDouble(new String(cbuf, start, len));
            }catch(NumberFormatException ex){}
        }
    }

    public void optimizeF() {
        /*
         *u is such that vett[i]=vett[u[i]]
         */
        ArrayList<Integer> u = new ArrayList<>(translator.size());

        /*
         * v is such that vett[v[i]]=vett[i]
         */
        ArrayList<Integer> v = new ArrayList<>(u.size());
        ArrayList<Integer> isVisited = F.transpose().scalar_product(
                new BitVector(F.ncolumns()));                            //isVisited contains the number of incoming arcs for each node
        for (int k = 0; k < translator.size(); k++)
            v.add(k);
        for (int i = 0; i < F.ncolumns(); i++)
            if (isVisited.get(i) == 0) {
                u.add(i);
                v.set(i, u.size() - 1);
            }
        for (int k = 0; k < u.size(); k++)
            for (int j = 0; j < F.ncolumns(); j++)
                if (F.get(u.get(k), j)) {
                    isVisited.set(j, isVisited.get(j) - 1);
                    if (isVisited.get(j) == 0) {
                        u.add(j);
                        v.set(j, u.size() - 1);
                    }
                }
        for (Integer visited:isVisited)
            if (visited > 0) {
                System.out.println("impossible optimizing F, the graph is not acyclic");
                return;
            }

        assert (BitMatrix.matrixFromIntArray(u).equals(BitMatrix.matrixFromIntArray(v).transpose()));
        UpperTriangularBitMatrixImpl F2 = new UpperTriangularBitMatrixImpl(u.size());
        for (int i = 0; i < F2.nrows(); i++)
            for (int j = 0; j < F2.ncolumns(); j++)
                F2.set(i, j, F.get(u.get(i), u.get(j)));
        //F=new UpperTriangular(BitMatrix.matrixFromIntArray(u).product(F.product(BitMatrix.matrixFromIntArray(v))));
        F = F2;
        //do permutation on translator data structure according permutation vector v. Where T[v[i]]:=T[i]
        translator.doPermutation(v);
    }
}

