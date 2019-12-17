package com.shalom.filemanager.filesystem.compressed.extractcontents;

import com.shalom.filemanager.filesystem.compressed.extractcontents.helpers.TarExtractor;

public class TarExtractorTest extends AbstractExtractorTest {
    @Override
    protected String getArchiveType() {
        return "tar";
    }

    @Override
    protected Class<? extends Extractor> extractorClass() {
        return TarExtractor.class;
    }
}
