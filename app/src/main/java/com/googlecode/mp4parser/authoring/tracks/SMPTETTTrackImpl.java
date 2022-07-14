package com.googlecode.mp4parser.authoring.tracks;

import com.coremedia.iso.Utf8;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.googlecode.mp4parser.authoring.AbstractTrack;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.googlecode.mp4parser.util.Iso639;
import com.microsoft.appcenter.Constants;
import com.mp4parser.iso14496.part30.XMLSubtitleSampleEntry;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/* loaded from: classes3.dex */
public class SMPTETTTrackImpl extends AbstractTrack {
    public static final String SMPTE_TT_NAMESPACE = "http://www.smpte-ra.org/schemas/2052-1/2010/smpte-tt";
    boolean containsImages;
    private long[] sampleDurations;
    TrackMetaData trackMetaData = new TrackMetaData();
    SampleDescriptionBox sampleDescriptionBox = new SampleDescriptionBox();
    XMLSubtitleSampleEntry XMLSubtitleSampleEntry = new XMLSubtitleSampleEntry();
    List<Sample> samples = new ArrayList();
    SubSampleInformationBox subSampleInformationBox = new SubSampleInformationBox();

    static long toTime(String expr) {
        Pattern p = Pattern.compile("([0-9][0-9]):([0-9][0-9]):([0-9][0-9])([\\.:][0-9][0-9]?[0-9]?)?");
        Matcher m = p.matcher(expr);
        if (m.matches()) {
            String hours = m.group(1);
            String minutes = m.group(2);
            String seconds = m.group(3);
            String fraction = m.group(4);
            if (fraction == null) {
                fraction = ".000";
            }
            String fraction2 = fraction.replace(Constants.COMMON_SCHEMA_PREFIX_SEPARATOR, ".");
            long ms = Long.parseLong(hours) * 60 * 60 * 1000;
            double parseLong = ms + (Long.parseLong(minutes) * 60 * 1000) + (Long.parseLong(seconds) * 1000);
            Double.isNaN(parseLong);
            long ms2 = (long) (parseLong + (Double.parseDouble("0" + fraction2) * 1000.0d));
            return ms2;
        }
        throw new RuntimeException("Cannot match " + expr + " to time expression");
    }

    public static String getLanguage(Document document) {
        return document.getDocumentElement().getAttribute("xml:lang");
    }

    public static long earliestTimestamp(Document document) {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        NamespaceContext ctx = new TextTrackNamespaceContext(null);
        XPath xpath = xPathfactory.newXPath();
        xpath.setNamespaceContext(ctx);
        try {
            XPathExpression timedNodesXpath = xpath.compile("//*[@begin]");
            NodeList timedNodes = (NodeList) timedNodesXpath.evaluate(document, XPathConstants.NODESET);
            long earliestTimestamp = 0;
            for (int i = 0; i < timedNodes.getLength(); i++) {
                Node n = timedNodes.item(i);
                String begin = n.getAttributes().getNamedItem("begin").getNodeValue();
                earliestTimestamp = Math.min(toTime(begin), earliestTimestamp);
            }
            return earliestTimestamp;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    public static long latestTimestamp(Document document) {
        XPathExpressionException e;
        long end;
        XPathFactory xPathfactory = XPathFactory.newInstance();
        NamespaceContext ctx = new TextTrackNamespaceContext(null);
        XPath xpath = xPathfactory.newXPath();
        xpath.setNamespaceContext(ctx);
        try {
            XPathExpression timedNodesXpath = xpath.compile("//*[@begin]");
            try {
                NodeList timedNodes = (NodeList) timedNodesXpath.evaluate(document, XPathConstants.NODESET);
                long lastTimeStamp = 0;
                for (int i = 0; i < timedNodes.getLength(); i++) {
                    Node n = timedNodes.item(i);
                    String begin = n.getAttributes().getNamedItem("begin").getNodeValue();
                    if (n.getAttributes().getNamedItem("dur") == null) {
                        if (n.getAttributes().getNamedItem(TtmlNode.END) != null) {
                            end = toTime(n.getAttributes().getNamedItem(TtmlNode.END).getNodeValue());
                        } else {
                            throw new RuntimeException("neither end nor dur attribute is present");
                        }
                    } else {
                        end = toTime(begin) + toTime(n.getAttributes().getNamedItem("dur").getNodeValue());
                    }
                    lastTimeStamp = Math.max(end, lastTimeStamp);
                }
                return lastTimeStamp;
            } catch (XPathExpressionException e2) {
                e = e2;
                throw new RuntimeException(e);
            }
        } catch (XPathExpressionException e3) {
            e = e3;
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public SMPTETTTrackImpl(File... files) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        super(r1[0].getName());
        DocumentBuilderFactory dbFactory;
        String firstLang;
        DocumentBuilder dBuilder;
        File[] fileArr = files;
        this.sampleDurations = new long[fileArr.length];
        DocumentBuilderFactory dbFactory2 = DocumentBuilderFactory.newInstance();
        dbFactory2.setNamespaceAware(true);
        DocumentBuilder dBuilder2 = dbFactory2.newDocumentBuilder();
        long startTime = 0;
        String firstLang2 = null;
        int sampleNo = 0;
        while (sampleNo < fileArr.length) {
            final File file = fileArr[sampleNo];
            SubSampleInformationBox.SubSampleEntry subSampleEntry = new SubSampleInformationBox.SubSampleEntry();
            this.subSampleInformationBox.getEntries().add(subSampleEntry);
            subSampleEntry.setSampleDelta(1L);
            Document doc = dBuilder2.parse(file);
            String lang = getLanguage(doc);
            if (firstLang2 == null) {
                firstLang2 = lang;
            } else if (!firstLang2.equals(lang)) {
                throw new RuntimeException("Within one Track all sample documents need to have the same language");
            }
            String firstLang3 = firstLang2;
            XPathFactory xPathfactory = XPathFactory.newInstance();
            NamespaceContext ctx = new TextTrackNamespaceContext(null);
            XPath xpath = xPathfactory.newXPath();
            xpath.setNamespaceContext(ctx);
            long lastTimeStamp = latestTimestamp(doc);
            this.sampleDurations[sampleNo] = lastTimeStamp - startTime;
            XPathExpression expr = xpath.compile("/ttml:tt/ttml:body/ttml:div/@smpte:backgroundImage");
            NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            HashMap<String, String> internalName2Original = new HashMap<>();
            Collection<String> originalNames = new HashSet<>();
            int i = 0;
            while (true) {
                dbFactory = dbFactory2;
                if (i >= nl.getLength()) {
                    break;
                }
                originalNames.add(nl.item(i).getNodeValue());
                i++;
                dbFactory2 = dbFactory;
                dBuilder2 = dBuilder2;
            }
            Collection<String> originalNames2 = new ArrayList<>(originalNames);
            Collections.sort((List) originalNames2);
            int p = 1;
            for (String originalName : originalNames2) {
                DocumentBuilder dBuilder3 = dBuilder2;
                String ext = originalName.substring(originalName.lastIndexOf("."));
                internalName2Original.put(originalName, "urn:dece:container:subtitleimageindex:" + p + ext);
                p++;
                dBuilder2 = dBuilder3;
                expr = expr;
                doc = doc;
            }
            if (!originalNames2.isEmpty()) {
                dBuilder = dBuilder2;
                String xml = new String(streamToByteArray(new FileInputStream(file)));
                for (Map.Entry<String, String> stringStringEntry : internalName2Original.entrySet()) {
                    XPathExpression expr2 = expr;
                    xml = xml.replace(stringStringEntry.getKey(), stringStringEntry.getValue());
                    expr = expr2;
                    doc = doc;
                    lang = lang;
                }
                final String finalXml = xml;
                final List<File> pix = new ArrayList<>();
                this.samples.add(new Sample() { // from class: com.googlecode.mp4parser.authoring.tracks.SMPTETTTrackImpl.1
                    @Override // com.googlecode.mp4parser.authoring.Sample
                    public void writeTo(WritableByteChannel channel) throws IOException {
                        channel.write(ByteBuffer.wrap(Utf8.convert(finalXml)));
                        for (File file1 : pix) {
                            FileInputStream fis = new FileInputStream(file1);
                            byte[] buffer = new byte[8096];
                            while (true) {
                                int n = fis.read(buffer);
                                if (-1 == n) {
                                    break;
                                }
                                channel.write(ByteBuffer.wrap(buffer, 0, n));
                            }
                        }
                    }

                    @Override // com.googlecode.mp4parser.authoring.Sample
                    public long getSize() {
                        long l = Utf8.convert(finalXml).length;
                        for (File file1 : pix) {
                            l += file1.length();
                        }
                        return l;
                    }

                    @Override // com.googlecode.mp4parser.authoring.Sample
                    public ByteBuffer asByteBuffer() {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        try {
                            writeTo(Channels.newChannel(baos));
                            return ByteBuffer.wrap(baos.toByteArray());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                SubSampleInformationBox.SubSampleEntry.SubsampleEntry xmlEntry = new SubSampleInformationBox.SubSampleEntry.SubsampleEntry();
                firstLang = firstLang3;
                xmlEntry.setSubsampleSize(Utf8.utf8StringLengthInBytes(finalXml));
                subSampleEntry.getSubsampleEntries().add(xmlEntry);
                for (Iterator<String> it = originalNames2.iterator(); it.hasNext(); it = it) {
                    File pic = new File(file.getParentFile(), it.next());
                    pix.add(pic);
                    SubSampleInformationBox.SubSampleEntry.SubsampleEntry sse = new SubSampleInformationBox.SubSampleEntry.SubsampleEntry();
                    sse.setSubsampleSize(pic.length());
                    subSampleEntry.getSubsampleEntries().add(sse);
                    finalXml = finalXml;
                    pix = pix;
                }
            } else {
                dBuilder = dBuilder2;
                firstLang = firstLang3;
                this.samples.add(new Sample() { // from class: com.googlecode.mp4parser.authoring.tracks.SMPTETTTrackImpl.2
                    @Override // com.googlecode.mp4parser.authoring.Sample
                    public void writeTo(WritableByteChannel channel) throws IOException {
                        Channels.newOutputStream(channel).write(SMPTETTTrackImpl.this.streamToByteArray(new FileInputStream(file)));
                    }

                    @Override // com.googlecode.mp4parser.authoring.Sample
                    public long getSize() {
                        return file.length();
                    }

                    @Override // com.googlecode.mp4parser.authoring.Sample
                    public ByteBuffer asByteBuffer() {
                        try {
                            return ByteBuffer.wrap(SMPTETTTrackImpl.this.streamToByteArray(new FileInputStream(file)));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
            sampleNo++;
            fileArr = files;
            startTime = lastTimeStamp;
            dbFactory2 = dbFactory;
            dBuilder2 = dBuilder;
            firstLang2 = firstLang;
        }
        this.trackMetaData.setLanguage(Iso639.convert2to3(firstLang2));
        this.XMLSubtitleSampleEntry.setNamespace(SMPTE_TT_NAMESPACE);
        this.XMLSubtitleSampleEntry.setSchemaLocation(SMPTE_TT_NAMESPACE);
        if (this.containsImages) {
            this.XMLSubtitleSampleEntry.setAuxiliaryMimeTypes("image/png");
        } else {
            this.XMLSubtitleSampleEntry.setAuxiliaryMimeTypes("");
        }
        this.sampleDescriptionBox.addBox(this.XMLSubtitleSampleEntry);
        this.trackMetaData.setTimescale(30000L);
        this.trackMetaData.setLayer(65535);
    }

    public byte[] streamToByteArray(InputStream input) throws IOException {
        byte[] buffer = new byte[8096];
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while (true) {
            int n = input.read(buffer);
            if (-1 != n) {
                output.write(buffer, 0, n);
            } else {
                return output.toByteArray();
            }
        }
    }

    @Override // com.googlecode.mp4parser.authoring.Track
    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.sampleDescriptionBox;
    }

    @Override // com.googlecode.mp4parser.authoring.Track
    public long[] getSampleDurations() {
        long[] adoptedSampleDuration = new long[this.sampleDurations.length];
        for (int i = 0; i < adoptedSampleDuration.length; i++) {
            adoptedSampleDuration[i] = (this.sampleDurations[i] * this.trackMetaData.getTimescale()) / 1000;
        }
        return adoptedSampleDuration;
    }

    @Override // com.googlecode.mp4parser.authoring.Track
    public TrackMetaData getTrackMetaData() {
        return this.trackMetaData;
    }

    @Override // com.googlecode.mp4parser.authoring.Track
    public String getHandler() {
        return "subt";
    }

    @Override // com.googlecode.mp4parser.authoring.Track
    public List<Sample> getSamples() {
        return this.samples;
    }

    @Override // com.googlecode.mp4parser.authoring.AbstractTrack, com.googlecode.mp4parser.authoring.Track
    public SubSampleInformationBox getSubsampleInformationBox() {
        return this.subSampleInformationBox;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }

    /* loaded from: classes3.dex */
    public static class TextTrackNamespaceContext implements NamespaceContext {
        private TextTrackNamespaceContext() {
        }

        /* synthetic */ TextTrackNamespaceContext(TextTrackNamespaceContext textTrackNamespaceContext) {
            this();
        }

        @Override // javax.xml.namespace.NamespaceContext
        public String getNamespaceURI(String prefix) {
            if (prefix.equals("ttml")) {
                return "http://www.w3.org/ns/ttml";
            }
            if (prefix.equals("smpte")) {
                return SMPTETTTrackImpl.SMPTE_TT_NAMESPACE;
            }
            return null;
        }

        @Override // javax.xml.namespace.NamespaceContext
        public Iterator getPrefixes(String val) {
            return Arrays.asList("ttml", "smpte").iterator();
        }

        @Override // javax.xml.namespace.NamespaceContext
        public String getPrefix(String uri) {
            if (uri.equals("http://www.w3.org/ns/ttml")) {
                return "ttml";
            }
            if (uri.equals(SMPTETTTrackImpl.SMPTE_TT_NAMESPACE)) {
                return "smpte";
            }
            return null;
        }
    }
}
