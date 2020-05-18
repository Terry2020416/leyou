package com.leyou.user.service;

import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import com.leyou.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired(required = false)
    private UserMapper userMapper;

    public static final String KEY_PREFIX = "user:code:phone:";

    @Autowired
    private StringRedisTemplate redisTemplate;

    public Boolean check(String data, Integer type) {
        System.out.println("data="+data);
        System.out.println("type="+type);
        System.out.println("userMapper="+userMapper);
        User user = new User();
        //type =1 表示验证用户名
        //type =2 表示验证手机号
        switch (type){
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
        }
        return this.userMapper.selectCount(user) != 1;//查到==1 说明注册过了，返回false，否则true
    }

    public Boolean sendVerifyCode(String phone) {
        //随机生成验证码
        //5 表示生成一个5位的随机数
        String code = NumberUtils.generateCode(5);
        System.out.println("code="+code);
        try{
            //把验证码存储到redies中并设置存活时间5分钟(用作验证用户输入验证码是否正确)
            ValueOperations<String, String> stringStringValueOperations =  this.redisTemplate.opsForValue();
            //user:code:phone:186xxxxxxxx,23596
            stringStringValueOperations.set(KEY_PREFIX+phone,code,5, TimeUnit.MINUTES);
            System.out.println("key:"+stringStringValueOperations.get(KEY_PREFIX+phone));
            return true;
        }catch (Exception e) {
            //logger.error("发送短信失败。phone：{}， code：{}", phone, code);
            e.printStackTrace();
            return false;
        }
    }
    //注册
    public Boolean register(User user, String code) {
        //校验验证码
        ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();
        //从redis根据key来获取验证码
        String s = stringStringValueOperations.get(KEY_PREFIX + user.getPhone());
        System.out.println("s="+s);
        if(!code.equals(s)){
            //验证码不正确
            return false;
        }
        user.setId(null);
        user.setCreated(new Date());
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));
        System.out.println("user="+user);
        //添加到数据库
        Boolean flag = this.userMapper.insertSelective(user)==1;
        System.out.println("flag：" + flag);
        if( flag){
            this.redisTemplate.delete(KEY_PREFIX+user.getPhone());
        }
        return flag;
    }

    public User queryUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        User user1 = this.userMapper.selectOne(user);
        //校验用户名
        if(user1 == null)
            return null;
        //校验密码
        if(!user1.getPassword().equals(CodecUtils.md5Hex(password,user1.getSalt()))){
            return null;
        }
        return user1;
    }
}
