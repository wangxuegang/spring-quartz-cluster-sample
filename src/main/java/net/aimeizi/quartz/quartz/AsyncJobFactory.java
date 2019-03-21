package net.aimeizi.quartz.quartz;

import java.io.IOException;

import net.aimeizi.quartz.model.ScheduleJob;
import net.aimeizi.quartz.vo.ScheduleJobVo;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * author : fengjing
 * createTime : 2016-08-04
 * description : 异步任务工厂
 * version : 1.0
 */
public class AsyncJobFactory extends QuartzJobBean {

    /* 日志对象 */
    private static final Logger LOG = LoggerFactory.getLogger(AsyncJobFactory.class);
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOG.info("AsyncJobFactory execute");
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get(ScheduleJobVo.JOB_PARAM_KEY);
        System.out.println("jobName:" + scheduleJob.getJobName() + "  " + scheduleJob);
        String url = scheduleJob.getUrl();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response;
        try {
            response = httpclient.execute(httpGet);
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            //EntityUtils.consume(entity);
            int state = response.getStatusLine().getStatusCode();
            System.out.println("响应状态:"+state+"\n内容："+EntityUtils.toString(entity,"UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
