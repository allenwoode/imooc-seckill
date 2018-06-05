// 存放秒杀交互逻辑
// 模块化

// JSON模块
var seckill = {
    URL: {
        now: function () {
           return '/seckill/now'; // 获取服务器当前时间，方便管理维护
        },
        exposer: function (id) {
            return '/seckill/' + id + '/exposer';
        },
        execution: function (id, md5) {
            return '/seckill/' + id + '/' + md5 + '/execution';
        }
    },
    validateCookie: function(phone) {
        return phone !== undefined;
    }, // 验证cookie内容
    validateInput: function(phone) {
        if (!phone) {
            $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号码为空！</label>').show(300);
            return false;
        } else if (phone.length !== 11 || isNaN(phone)) {
            $('#killPhoneMessage').hide().html('<label class="label label-danger">手机格式有误！</label>').show(300);
            return false
        }
        return true;
    }, // 验证输入表单
    countdown: function (seckillId, nowTime, startTime, deadTime) {
        var seckillBox = $('#seckill-box');
        //console.log('now %d start %d dead %d', nowTime, startTime, deadTime);
        if (nowTime > deadTime) {
            seckillBox.html('秒杀结束')
        } else if (nowTime < startTime) {
            // 秒杀进入倒计时
            seckillBox.countdown(startTime+1000, function (event) {
                var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown', function () {
                // 倒计时结束触发事件
                seckill.handleSeckillReady(seckillId, seckillBox);
            });
        } else {
            // 秒杀执行
            seckill.handleSeckillReady(seckillId, seckillBox);
        }
    }, // 秒杀倒计时逻辑
    handleSeckillReady: function (id, node) {
        node.hide().html('<button class="btn btn-success btn-lg" id="seckillBtn">开始秒杀</button>');
        // 请求秒杀地址并进行秒杀准备
        $.post(seckill.URL.exposer(id), {}, function (result) {
           if (result && result['success']) {
               node.show(); // 显示秒杀操作按钮
               var exposer = result['data'];
               if (exposer['exposed']) {
                   // 服务端确定秒杀开启
                   var md5 = exposer['md5'];
                   // 注册秒杀按钮点击事件，注意一次性注册
                   $('#seckillBtn').one('click', function () {
                       // 1 disable button
                       $(this).addClass('disabled');

                       // 2 提交秒杀请求
                       $.post(seckill.URL.execution(id, md5), {}, function (result) {
                           if (result && result['success']) {
                                var state = result['data']['state'];
                                var stateInfo = result['data']['stateInfo'];
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                           } else {
                               // 用户cookie过期
                               window.location.reload();
                           }
                       });
                   });
               } else {
                   // 服务器并不认为秒杀开启
                   var now = exposer['now'];
                   var start = exposer['start'];
                   var dead = exposer['dead'];
                   // 重新开启倒计时
                   seckill.countdown(id, now, start, dead);
               }

           } else {
               console.log('result: ' + result);
           }
        });
    }, // 秒杀准备获取秒杀URL
    // 详情页交互逻辑
    detail: {
        init: function (params) {
            //$('#killPhoneModal').show();
            var killPhone = params['killPhone'];

            // 手机号码验证
            if (seckill.validateCookie(killPhone)) {
                // cookie验证成功，执行秒杀
                var startTime = params['startTime'];
                var deadTime = params['deadTime'];
                var seckillId = params['seckillId'];
                $.get(seckill.URL.now(), {}, function (result) {
                    if (result && result['success']) {
                        var nowTime = result['data'];
                        seckill.countdown(seckillId, nowTime, startTime, deadTime);
                    }
                });
            } else {
                // cookie验证登陆失败，弹出登陆窗口
                $('#killPhoneModal').modal({
                    show: true,
                    keyboard: false,
                    backdrop: 'static'
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validateInput(inputPhone)) {
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        window.location.reload();
                    }
                });
            }
        }
    }
};