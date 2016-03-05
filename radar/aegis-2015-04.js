document.title = "宙斯盾技术雷达 (2015年4月)";
var radar_arcs = [  {'r': 100, 'name': '稳定'}  , {'r': 200, 'name': '尝试↑'} , {'r': 300, 'name': '技术♥债'}  , {'r': 400, 'name': '搁置'} , {'r': 500, 'name': '抛弃↓'}];
var w = 1500;
var h = 1200;
var left_1=65;
var left_2= -165;
var radar_data = [
    {
        "quadrant": "理论·方法",
        "left": left_1,
        "top": 18,
        "color": "#0099CC",
        "items": [
			/*半径 角度 */
            {name: '持续集成', pc: {"r": 30 , "t": 100}, movement: 'c'},
            {name: '分层测试', pc: {"r": 30 , "t": 130}, movement: 'c'},
            {name: 'UI自动化', pc: {"r": 30 , "t": 160}, movement: 'c'},
			{name: 'RESTful（HTTP API）', pc: {"r": 130 , "t": 130}, movement: 'c'},
			{name: '容器化', pc: {"r": 170 , "t": 130}, movement: 'c'},
            {name: '持续交付', pc: {"r": 230 , "t": 95}, movement: 'c'},
            {name: '敏捷', pc: {"r": 230 , "t": 105}, movement: 'c'},
            {name: '探索性测试', pc: {"r": 230 , "t": 115}, movement: 'c'},
            {name: '前后端分离', pc: {"r": 230 , "t": 125}, movement: 'c'},
            {name: '单网页应用', pc: {"r": 230 , "t": 135}, movement: 'c'},
            {name: 'DDD', pc: {"r": 230 , "t": 145}, movement: 'c'},
            {name: '微服务', pc: {"r": 230 , "t": 155}, movement: 'c'},
            {name: '容量规划', pc: {"r": 230 , "t": 165}, movement: 'c'},
        ]
    },
    {
        "quadrant": "工具",
        "left": (w + left_2),
        "top": 18,
        "color": "#FF6633",
        "items": [
            {name: 'Markdown', pc: {r: 40, t: 15}, movement: 'c' },
            {name: 'Gliffy Diagrams', pc: {r: 40, t: 35}, movement: 'c' },
            {name: 'Xmind', pc: {r: 40, t: 55}, movement: 'c' },
            {name: 'VI', pc: {r: 40, t: 75}, movement: 'c' },
            {name: 'Sublime Text', pc: {r: 70, t: 10}, movement: 'c' },
            {name: 'Maven', pc: {r: 70, t: 30}, movement: 'c' },
            {name: 'IDEA', pc: {r: 70, t: 50}, movement: 'c' },
            {name: 'Eclipse', pc: {r: 70, t: 70}, movement: 'c' },
            {name: 'Virtual Box', pc: {r: 90, t: 15}, movement: 'c' },
            {name: 'Navicat', pc: {r: 90, t: 30}, movement: 'c' },
            {name: 'XShell (Windows)', pc: {r: 90, t: 45}, movement: 'c' },

            {name: 'Git', pc: {r: 120, t: 20}, movement: 'c' },
            {name: 'Dash (Mac)', pc: {r: 140, t: 40}, movement: 'c' },
            {name: 'Sequel Pro (Mac)', pc: {r: 160, t: 60}, movement: 'c' },

            {name: 'Atom', pc: {r: 230, t: 20}, movement: 'c' },
            {name: 'Webstorm', pc: {r: 250, t: 35}, movement: 'c' },
            {name: 'liteIDE', pc: {r: 270, t: 10}, movement: 'c' },
            {name: 'Gradle', pc: {r: 270, t: 30}, movement: 'c' },
            {name: 'dia', pc: {r: 270, t: 50}, movement: 'c' },
            {name: 'Pencil', pc: {r: 270, t: 60}, movement: 'c' },
            {name: 'Docker', pc: {r: 270, t: 70}, movement: 'c' },

            {name: 'Evernote', pc: {r: 330, t: 20}, movement: 'c' },
            {name: 'NotePad++ (Windows)', pc: {r: 330, t: 40}, movement: 'c' },
            {name: 'SVN', pc: {r: 330, t: 70}, movement: 'c' },

            {name: 'Ant', pc: {r: 430, t: 10}, movement: 'c' },
            {name: 'Visio', pc: {r: 430, t: 40}, movement: 'c' },
            {name: 'SecureCRT', pc: {r: 430, t: 70}, movement: 'c' }
        ]
    },
    {
        "quadrant": "中间件·平台",
        "left": left_1,
        "top": (h / 2 + 18),
        "color": "#669966",
        "items": [
            {name: 'Redis', pc: {"r": 30, "t": 190}, movement: 'c'},
            {name: 'Jenkins', pc: {"r": 30, "t": 220}, movement: 'c'},
            {name: 'Jetty', pc: {"r": 30, "t": 250}, movement: 'c'},
            {name: 'ActiveMQ', pc: {"r": 60, "t": 200}, movement: 'c'},
            {name: 'BUC', pc: {"r": 60, "t": 230}, movement: 'c'},
            {name: 'HAProxy', pc: {"r": 60, "t": 260}, movement: 'c'},

            {name: 'Zabbix', pc: {"r": 130, "t": 250}, movement: 'c'},

            {name: 'Notify', pc: {"r": 230, "t": 190}, movement: 'c'},
            {name: 'ZeroMQ', pc: {"r": 230, "t": 210}, movement: 'c'},
            {name: 'NanoMsg', pc: {"r": 230, "t": 230}, movement: 'c'},
            {name: 'HSF', pc: {"r": 270, "t": 250}, movement: 'c'},
            {name: 'GRPC', pc: {"r": 230, "t": 250}, movement: 'c'},
            {name: 'Aone2', pc: {"r": 250, "t": 200}, movement: 'c'},
            {name: 'Nginx', pc: {"r": 250, "t": 220}, movement: 'c'},
            {name: 'ODPS', pc: {"r": 250, "t": 260}, movement: 'c'},

            {name: 'Dubbo', pc: {"r": 330, "t": 210}, movement: 'c'},
            {name: 'Napoli', pc: {"r": 370, "t": 260}, movement: 'c'},

            {name: 'RabbitMQ', pc: {"r": 430, "t": 185}, movement: 'c'},
            {name: 'Honetq', pc: {"r": 430, "t": 205}, movement: 'c'},
            {name: 'Memcache', pc: {"r": 430, "t": 225}, movement: 'c'},
            {name: 'Apache', pc: {"r": 430, "t": 245}, movement: 'c'},
            {name: 'Tomcat', pc: {"r": 430, "t": 265}, movement: 'c'}
        ]
    },
    {
        "quadrant": "语言·框架",
        "color": "#B70062",
        "left": (w + left_2),
        "top": (h / 2 + 18),
        "items": [
            {name: 'Jedis Pool', pc: {"r": 45 , "t": 290}, movement: 'c'},
            {name: 'Selenuim', pc: {"r": 45 , "t": 315}, movement: 'c'},
            {name: 'Shell', pc: {"r": 45, "t": 340}, movement: 'c'},
            {name: 'logback', pc: {"r": 65 , "t": 285}, movement: 'c'},
            {name: 'nodejs', pc: {"r": 65, "t": 305}, movement: 'c'},
            {name: 'logback', pc: {"r": 65 , "t": 325}, movement: 'c'},
            {name: 'Bootstrap', pc: {"r": 65, "t": 345}, movement: 'c'},

            {name: 'CasperJS', pc: {"r": 120 , "t": 280}, movement: 'c'},
            {name: 'angularjs', pc: {"r": 120 , "t": 295}, movement: 'c'},
            {name: 'mybatis', pc: {"r": 120 , "t": 310}, movement: 'c'},
            {name: 'Go', pc: {"r": 150 , "t": 325}, movement: 'c'},
            {name: 'Spring boot', pc: {"r": 120 , "t": 340}, movement: 'c'},

            {name: 'Java 7', pc: {"r": 250 , "t": 285}, movement: 'c'},
            {name: 'Java 8', pc: {"r": 250 , "t": 300}, movement: 'c'},
            {name: 'Spring 4', pc: {"r": 250 , "t": 315}, movement: 'c'},
            {name: 'Log4j2', pc: {"r": 250 , "t": 330}, movement: 'c'},
            {name: 'HTML5', pc: {"r": 250 , "t": 345}, movement: 'c'},

            {name: 'Webx', pc: {"r": 350 , "t": 300}, movement: 'c'},
            {name: 'Ruby', pc: {"r": 350 , "t": 340}, movement: 'c'},
        ]
    }
];
