package com.shalom.filemanager.filesystem.compressed.extractcontents;

import com.shalom.filemanager.filesystem.compressed.extractcontents.helpers.GzipExtractor;

public class TgzExtractorTest extends AbstractExtractorTest {
    @Override
    protected String getArchiveType() {
        return "tgz";
    }

    @Override
    protected Class<? extends Extractor> extractorClass() {
        return GzipExtractor.class;
    }
}
