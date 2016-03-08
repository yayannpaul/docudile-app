package com.docudile.app.services;

import com.docudile.app.services.impl.DocumentStructureClassificationServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * Created by FrancAnthony on 3/1/2016.
 */
public interface DocumentStructureClassificationService {

    public Map<Integer, String> tag(String path, List<String> lines);

    public String classify(String query, String dataPath);

    public boolean trainTagger(String path, String tagType, String displayName, List<String> lines);

    public boolean trainClassifier(String path, List<String> tags, String type);

    public boolean delete(String path);

}
