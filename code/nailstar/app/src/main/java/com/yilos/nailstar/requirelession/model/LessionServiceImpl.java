package com.yilos.nailstar.requirelession.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.yilos.nailstar.aboutme.model.LoginAPI;
import com.yilos.nailstar.framework.exception.NotLoginException;
import com.yilos.nailstar.requirelession.entity.CandidateLession;
import com.yilos.nailstar.requirelession.entity.LessionActivity;
import com.yilos.nailstar.requirelession.entity.VideoLession;
import com.yilos.nailstar.requirelession.entity.VotedRecord;
import com.yilos.nailstar.util.FileUtils;
import com.yilos.nailstar.util.HttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by yilos on 15/10/26.
 */
public class LessionServiceImpl implements LessionService {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public LessionActivity queryLessionActivity() throws IOException, JSONException {

        String stringResult = HttpClient.getJson("/vapi/nailstar/qjc/activity");

        JSONObject jsonResult = new JSONObject(stringResult);

        if (jsonResult.getInt("code") != 0) {
            throw new IOException("Unexpected code");
        }

        JSONObject jsonActivity = jsonResult.getJSONObject("result");

        LessionActivity lessionActivity = new LessionActivity();

        lessionActivity.setStage(jsonActivity.getInt("stage"));
        lessionActivity.setEndTime(jsonActivity.optLong("endTime"));
        lessionActivity.setNo(jsonActivity.getInt("no"));

        String activityTopic = jsonActivity.optString("previous");
        if (!activityTopic.isEmpty()) {
            lessionActivity.setPrevious(objectMapper.readValue(activityTopic, VideoLession.class));
        }
        activityTopic = jsonActivity.optString("current");
        if (!activityTopic.isEmpty()) {
            lessionActivity.setCurrent(objectMapper.readValue(activityTopic, CandidateLession.class));
        }

        return lessionActivity;
    }

    @Override
    public List<CandidateLession> queryVoteLessionList() throws IOException, JSONException {

        String stringResult = HttpClient.getJson("/vapi/nailstar/qjc/random");

        JSONObject jsonResult = new JSONObject(stringResult);

        if (jsonResult.getInt("code") != 0) {
            throw new IOException("Unexpected code");
        }

        String stringCandidates = jsonResult.getJSONObject("result").optString("candidates");

        List<CandidateLession> candidates = objectMapper.readValue(stringCandidates, new TypeReference<List<CandidateLession>>() {
        });

        return candidates;
    }

    @Override
    public List<CandidateLession> queryRankingLessionList(int page) throws IOException, JSONException {

        String url = "/vapi/nailstar/qjc/ranking?page=" + page;
        String stringResult = HttpClient.getJson(url);

        JSONObject jsonResult = new JSONObject(stringResult);

        if (jsonResult.getInt("code") != 0) {
            throw new IOException("Unexpected code");
        }

        String stringCandidates = jsonResult.getJSONObject("result").optString("candidates");

        List<CandidateLession> candidates = objectMapper.readValue(stringCandidates, new TypeReference<List<CandidateLession>>() {
        });

        return candidates;
    }

    @Override
    public List<VideoLession> queryHistoryLessionList(int page) throws IOException, JSONException {

        String url = "/vapi/nailstar/qjc/history?page=" + page;
        String stringResult = HttpClient.getJson(url);

        JSONObject jsonResult = new JSONObject(stringResult);

        if (jsonResult.getInt("code") != 0) {
            throw new IOException("Unexpected code");
        }

        String stringHistory = jsonResult.getJSONObject("result").optString("history");

        List<VideoLession> history = objectMapper.readValue(stringHistory, new TypeReference<List<VideoLession>>() {
        });

        return history;
    }

    @Override
    public void vote(String id) throws IOException, JSONException {

        String url = "/vapi/nailstar/qjc/candidate/" + id + "/vote";
        String stringResult = HttpClient.post(url, "{}");

        JSONObject jsonResult = new JSONObject(stringResult);

        if (jsonResult.getInt("code") != 0) {
            throw new IOException("Unexpected code");
        }

    }

    @Override
    public void reportIllegal(String id) throws IOException, JSONException {
        String url = "/vapi/nailstar/qjc/candidate/" + id + "/report";
        String stringResult = HttpClient.post(url, "{}");

        JSONObject jsonResult = new JSONObject(stringResult);

        if (jsonResult.getInt("code") != 0) {
            throw new IOException("Unexpected code");
        }
    }

    @Override
    public VotedRecord queryVotedRecord(File fileName) throws IOException, JSONException {

        if (!fileName.exists()) {
            return null;
        }

        String stringResult = FileUtils.readFromFile(fileName);
        JSONObject jsonResult = new JSONObject(stringResult);

        VotedRecord votedRecord = new VotedRecord();
        votedRecord.setNo(jsonResult.getInt("no"));
        String stringCandidateIdList = jsonResult.optString("candidateIdList");
        List<String> candidateIdList = objectMapper.readValue(stringCandidateIdList, new TypeReference<List<String>>() {
        });
        votedRecord.setCandidateIdList(candidateIdList);

        return votedRecord;
    }

    @Override
    public void saveVotedRecord(File fileName, VotedRecord votedRecord) throws IOException {
        String stringVotedRecord = objectMapper.writeValueAsString(votedRecord);
        FileUtils.writeToFile(fileName, stringVotedRecord);
    }

    @Override
    public void postCandidate(String imageUrl) throws IOException, JSONException, NotLoginException {

        if (imageUrl == null || "".equals(imageUrl)) {
            return;
        }

        String url = "/vapi/nailstar/qjc/candidate";

        String uid = LoginAPI.getInstance().getLoginUserId();
        if (uid == null) {
            throw new NotLoginException("not login");
        }
        RequestBody formBody = new FormEncodingBuilder().add("picUrl", imageUrl).add("uid", uid).build();

        String stringResult = HttpClient.post(url, formBody);

        JSONObject jsonResult = new JSONObject(stringResult);

        if (jsonResult.getInt("code") != 0) {
            throw new IOException("Unexpected code");
        }
    }
}
