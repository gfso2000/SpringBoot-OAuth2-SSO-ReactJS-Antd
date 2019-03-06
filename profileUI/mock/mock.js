// 代码中会兼容本地 service mock 以及部署站点的静态数据
import { delay } from 'roadhog-api-doc';

const proxy = {
  'POST /api/editprofile': (req, res) => {
    let temp = new Date().getTime()%2;
    if(temp==0){
      res.send({ status: 'fail', errorMessage: 'user already exists' });
    }else{
      res.send({ status: 'success' });
    }
  },
  'GET /api/getusername': (req, res) => {
    let temp = new Date().getTime()%2;
    if(temp==0){
      res.send({ status: 'fail', errorMessage: 'failed to get username' });
    }else{
      res.send({ status: 'success', errorMessage: 'jack.yu05@sap.com' });
    }
  },
};

// 调用 delay 函数，统一处理
export default delay(proxy, 3000);