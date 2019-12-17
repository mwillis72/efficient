package com.shalom.filemanager.filesystem.compressed.extractcontents;

import com.shalom.filemanager.filesystem.compressed.extractcontents.helpers.XzExtractor;

public class XzExtractorTest extends AbstractExtractorTest {
    @Override
    protected String getArchiveType() {
        return "tar.xz";
    }

    @Override
    protected Class<? extends Extractor> extractorClass() {
        return XzExtractor.class;
    }
}
