package com.yilos.nailstar.requirelession.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yilos.nailstar.requirelession.entity.CandidateLession;
import com.yilos.nailstar.requirelession.entity.LessionActivity;
import com.yilos.nailstar.requirelession.entity.VideoLession;
import com.yilos.nailstar.util.HttpClient;

import org.json.JSONException;
import org.json.JSONObject;

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
}
