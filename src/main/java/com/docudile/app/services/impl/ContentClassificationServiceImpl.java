package com.docudile.app.services.impl;

import com.docudile.app.data.dao.*;
import com.docudile.app.data.dao.impl.WordListDocumentDaoImpl;
import com.docudile.app.data.dto.CategoryDto;
import com.docudile.app.data.dto.FileContentDto;
import com.docudile.app.data.dto.WordListDto;
import com.docudile.app.data.entities.*;
import com.docudile.app.services.ContentClassificationService;
import com.docudile.app.services.DocxService;
import com.docudile.app.services.FileSystemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cicct on 2/15/2016.
 */
@Service("contentClassificationService")
@Transactional
@PropertySource({"classpath:/storage.properties"})
public class ContentClassificationServiceImpl implements ContentClassificationService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private Environment environment;

    @Autowired
    private FileDao fileDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private DocxService docxService;

    @Autowired
    private WordListDao wordListDao;

    @Autowired
    private WordListCategoryDao wordListCategoryDao;

    public boolean train(Integer userID) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        WordListDto wordList = null;
        List<FileContentDto> fileDto = new ArrayList<>();
        List<com.docudile.app.data.entities.File> file = new ArrayList<>();
        CategoryDto category = new CategoryDto();
        FileContentDto fileContentDto = new FileContentDto();
        List<CategoryDto> categoriesDto = new ArrayList<>();

        //get Categories from DB
        List<Category> categories = categoryDao.getCategories(userID);
        System.out.println(categories.size() + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        for (int x = 0; x < categories.size(); x++) {
            //instantiate a Category Class ( new Category(categoryName,fileCount) )
            category = new CategoryDto();
            System.out.println(categories.get(x).getCategoryName() + " EEEEEEEEE");
            category.setName(categories.get(x).getCategoryName());
            category.setCategoryID(categories.get(x).getId());
            //add it into List<Category> categories
            categoriesDto.add(category);
        }

        User user = userDao.getUserDetails(userID);

        for(int x = 0;x<categoriesDto.size();x++) {
            System.out.println(categoriesDto.get(x).getName() + " @@@@@@@@@@@@@@@@@@@ ");
            java.io.File folder = new java.io.File(environment.getProperty("storage.users") + userDao.show(userID).getUsername() + "//" +environment.getProperty("storage.content_training")+"//"+categoriesDto.get(x).getName());
            for (final java.io.File fileEntry : folder.listFiles()) {
                fileContentDto = new FileContentDto();
                fileContentDto.setFileName(fileEntry.getName());
                fileContentDto.setCategoryName(categoriesDto.get(x).getName());
                fileContentDto.setWordList(docxService.readDocx(fileEntry));
                fileDto.add(fileContentDto);
            }
        }

//        category.setName("SampleCategory");
//        category.setFileCount(2);
//        category.setCategoryID(1);
//        categoriesDto.add(category);

        //User user = userDao.getUserDetails(userID);

//        for(int x = 0;x<categoriesDto.size();x++) {
//            //java.io.File folder = new java.io.File(environment.getProperty("storage.users") + userDao.show(userID).getUsername() + "/" + environment.getProperty("storage.content_training") + "/" + categoriesDto.get(x).getName());
//            java.io.File folder = new java.io.File("C:\\Docudile\\TestingFiles");
//            for (final java.io.File fileEntry : folder.listFiles()) {
//                System.out.println(fileEntry);
//                FileContentDto fileContentDto = new FileContentDto();
//                fileContentDto.setFileName(fileEntry.getName());
//                fileContentDto.setCategoryName(categoriesDto.get(x).getName());
//                fileContentDto.setWordList(docxService.readDocx(fileEntry));
//                fileDto.add(fileContentDto);
//            }
//        }

        //get wordList in DB
        wordList.setWordList(wordListDao.getWords());
        wordList.setCount(wordListDao.getWords().size());


        //get updated wordList
        wordList = getDistinctWords(fileDto, wordList);
        //end

        //count words
        categoriesDto = countWords(fileDto, wordList, categoriesDto);
        //end
        // get vectors
        float[][] wordListVectors = calculateNaiveBayes(wordList, categoriesDto);
        //end


        //put to DB si wordListVectors
        for(int x = 0;x<wordListVectors.length;x++){
            for(int y = 0;y<wordListVectors[0].length;y++){
                WordListCategory wordListCategory = new WordListCategory();
                wordListCategory.setWordList(wordListDao.getID(wordList.getWordList().get(x)));
                wordListCategory.setCategory(categoryDao.getCategory(categoriesDto.get(y).getCategoryID()));
                wordListCategory.setCount(wordListVectors[x][y]);
                wordListCategoryDao.create(wordListCategory);
            }
        }
        return true;
    }


    public WordListDto getDistinctWords(List<FileContentDto> files, WordListDto list) {
        boolean isExist = false;
        List<String> wordListWords = list.getWordList();
        for (int x = 0; x < files.size(); x++) {
            List<String> words = files.get(x).getWordList();
            for (int y = 0; y < words.size(); y++) {
                for (int z = 0; z < wordListWords.size(); z++) {
                    if ((words.get(y).equalsIgnoreCase(wordListWords.get(z)))) {
                        isExist = true;
                        break;
                    }
                }
                if(!isExist){
                    WordList word = new WordList();
                    wordListWords.add(words.get(y));
                    word.setWord(words.get(y));
                    wordListDao.create(word);
                }
                isExist = false;
            }
        }
        WordListDto wordList = new WordListDto(wordListWords);
        return wordList;
    }

    public List<CategoryDto> countWords(List<FileContentDto> files, WordListDto wordList, List<CategoryDto> categories) {

        String[][] countWords = new String[wordList.getWordList().size()][2];
        List<String> wordListWords = wordList.getWordList();
        List<String> filesWordList = null;
        int counter = 0;
        for (int x = 0; x < wordListWords.size(); x++) {
            countWords[x][0] = wordListWords.get(x);
            countWords[x][1] = "0";
        }
        for (int x = 0; x < categories.size(); x++) {
            categories.get(x).setWordList(countWords);
        }
        for (int x = 0; x < files.size(); x++) {
            filesWordList = files.get(x).getWordList();
            for (int y = 0; y < categories.size(); y++) {
                if (categories.get(y).getName().equalsIgnoreCase(files.get(x).getCategoryName())) {
                    counter = y;
                }
            }
            countWords = categories.get(counter).getWordList();
            for (int a = 0; a < filesWordList.size(); a++) {
                for (int z = 0; z < wordListWords.size(); z++) {
                    if (filesWordList.get(a).equalsIgnoreCase(wordListWords.get(z))) {
                        categories.get(counter).addWordCount();
                        countWords[z][1] = (Integer.parseInt(countWords[z][1]) + 1) + "";
                    }
                }
            }
            categories.get(counter).setWordList(countWords);

        }


        return categories;
    }

    public float[][] calculateNaiveBayes(WordListDto wordList, List<CategoryDto> categories) {
        List<String> wordListWords = wordList.getWordList();
        float[][] wordListVectors = new float[wordListWords.size()][categories.size()];
        int numOfDistinctWords = wordListWords.size();
        for (int x = 0; x < categories.size(); x++) {
            String[][] tempCategoryVector = categories.get(x).getWordList();
            for (int y = 0; y < wordListWords.size(); y++) {
                wordListVectors[y][x] = ((float) (Float.parseFloat(tempCategoryVector[y][1]) + 1)) / ((float) (categories.get(x).getWordCount() + numOfDistinctWords));
            }
        }
        return wordListVectors;
    }

    public Integer categorize( List<String> words, Integer userID, String filename) {
        List<CategoryDto> categories = null;
        //get data from DB Category
        List<Category> categoryList = categoryDao.getCategories(userID);
        float[] categoryVectors = new float[categories.size()];

        CategoryDto categoryDto = new CategoryDto();
        for(int x = 0;x<categoryList.size();x++){
            categoryDto.setName(categoryList.get(x).getCategoryName());
            categoryDto.setFileCount(fileDao.numberOfFiles(categoryList.get(x).getId()));
            categoryDto.setCategoryID(categoryList.get(x).getId());
            categories.add(categoryDto);
        }
        //instantiate a Category Class ( new Category(categoryName,fileCount) )
        //add Categories in List<Category> categories
        int totalFiles = 0;

        WordListDto wordList = null;
        wordList.setWordList(wordListDao.getWords());
        wordList.setCount(wordListDao.getWords().size());

        FileContentDto fileContentDto = null;
        fileContentDto.setWordList(words);
        fileContentDto.setFileName(filename);

        wordList = getDistinctWords((List<FileContentDto>) fileContentDto,wordList);



        for (int x = 0; x < categories.size(); x++) {
            totalFiles += categories.get(x).getFileCount();
        }

        for (int x = 0; x < categories.size(); x++) {
            categoryVectors[x] = categories.get(x).getFileCount() / totalFiles;
        }

        for(int x = 0;x<words.size();x++){
            for(int y = 0;y<categoryVectors.length;y++) {
                categoryVectors[y] *= wordListCategoryDao.getVector(categoryList.get(y).getId(), words.get(x));
            }
        }


        categoryDto = categories.get(0);
        float temp = categoryVectors[0];
        for(int x = 0;x<categoryVectors.length;x++){
            if(categoryVectors[x] <   temp){
                categoryDto = categories.get(x);
                temp = categoryVectors[x];
            }
        }

        return categoryDto.getCategoryID();
    }


    public String readDocxFile(String fileName) {
        String sentence = "";
        try {
            java.io.File file = new java.io.File(fileName);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());

            XWPFDocument document = new XWPFDocument(fis);

            List<XWPFParagraph> paragraphs = document.getParagraphs();

            for (XWPFParagraph para : paragraphs) {
                sentence += para.getText();
            }
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sentence;
    }

    private void getCount(List<FileContentDto> files, WordListDto wordList, Integer fileID){
        String[][] wordCount = new String[wordList.getWordList().size()][2];
        WordListDocument wordListDocument = null;
        WordListDocumentDaoImpl wordListDocumentDao = null;

        for(int x = 0;x<wordList.getWordList().size();x++){
            wordCount[x][0] = wordList.getWordList().get(x);
            wordCount[x][1] = "0";

        }
        for(int x = 0;x<files.get(0).getWordList().size();x++){
            for(int y = 0;y<wordCount.length;y++){
                if(wordCount[y][0].equalsIgnoreCase(files.get(0).getWordList().get(x))){
                    wordCount[y][1] = ((Integer.parseInt(wordCount[y][1])+1)) + "";
                }

            }
        }
        for(int x = 0;x<wordCount.length;x++){
            wordListDocument.setWordList(wordListDao.show(wordListDao.getID(wordCount[x][0]).getId()));
            wordListDocument.setFile(fileDao.show(fileID));
            wordListDocument.setVectorCount(Integer.parseInt(wordCount[x][1]));
            wordListDocumentDao.create(wordListDocument);
        }

    }

    public void writeToFile(MultipartFile[] f, String path) throws IOException {

        System.out.println(f.toString() + " ###########");
        for(int x = 0;x<f.length;x++){
            File file = new File(path+"//"+f[x].getOriginalFilename());
            file.getParentFile().mkdirs();
            f[x].transferTo(file);
        }

    }

    public void createCategory(String categoryName, Integer userID){
        Category cat = new Category();
        cat.setCategoryName(categoryName);
        cat.setUser(userDao.getUserDetails(userID));
        cat.setId(1);
        categoryDao.create(cat);
        System.out.println(categoryName+"created Category");
    }

}
