package com.hanjiang.etm.common.dto;

/**
 * 描述：试题配置
 *
 * @author huxuehao
 **/
public abstract class QuestionConfig {
    // 分类
    private Long classifyId;
    // 名称
    private String name;
    // 题干
    private String title;
    // 解析
    private String analysis;
    // 分数
    private Integer score;
    // 难度
    private Integer level;
    // 是否作答
    private boolean already;
    // 类型
    private Integer type;
    // 类型
    private Integer success;
    private Integer share;
    private String rowKey;


    public Long getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(Long classifyId) {
        this.classifyId = classifyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public Integer getScore() {
        return score;
    }
    public Integer getScoreBySuccess() {
        if (success != null && success == 1) {
            return score;
        }
        return 0;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public boolean isAlready() {
        return already;
    }

    public void setAlready(boolean already) {
        this.already = already;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    /**
     * 是否默认批改
     * 当试卷时人工审批时，对于其中的一些题型可以进行默认批改，
     * 以减少批改人员的工作量，对于默认批改的题目，批改人可以
     * 进行任意的修改审批结果
     */
    public abstract boolean defaultAutoCorrect();

    /**
     * 转换成JSON
     */
    public abstract String toJson();

    /**
     * 将JSON转成Config
     * @param jsonConfig 字符串
     */
    public abstract QuestionConfig toConfig(String jsonConfig);

    /**
     * 批改
     */
    public abstract Integer doCorrect();

    /**
     * 得分
     */
    public abstract Integer genScore();

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }
}
