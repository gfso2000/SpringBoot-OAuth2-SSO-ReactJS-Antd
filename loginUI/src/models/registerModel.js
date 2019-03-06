import { fakeRegister,fakeResetPassword,fakeGetLoginQR,fakeCheckLoginQR } from '@/services/apiServices';
import {delay} from '@/utils/request';

export default {
  namespace: 'register',

  state: {
    status: undefined,
  },

  effects: {
    *submit({ payload }, { call, put }) {
      const response = yield call(fakeRegister, payload);
      yield put({
        type: 'registerHandle',
        payload: response,
      });
    },
    *resetPassword({ payload }, { call, put }) {
      const response = yield call(fakeResetPassword, payload);
      yield put({
        type: 'resetPasswordHandle',
        payload: response,
      });
    },
    *getLoginQR({ payload }, { call, put, select }) {
      const response = yield call(fakeGetLoginQR, payload);
      yield put({
        type: 'getLoginQRHandle',
        payload: response,
      });

      //after 4 seconds, check qr is scaned or not
      yield call(delay, 4000);
      yield put({ type: 'checkLoginQR', });
    },
    *checkLoginQR({ payload }, { call, put, select }) {
      const {randomUUID} = yield select(_ => _.register);
      const {status} = yield call(fakeCheckLoginQR, {randomUUID});
      if(status=='fail'){
        yield call(delay, 4000);
        yield put({ type: 'checkLoginQR', });
      }else{
        yield put({
          type: 'checkLoginQRHandle',
          payload: status,
        });
      }
    },
  },

  reducers: {
    reset(state, { payload }) {
      return {
        ...state,
        status: undefined,
      };
    },
    registerHandle(state, { payload }) {
      return {
        ...state,
        status: payload.status,
        errorMessage: payload.errorMessage,
      };
    },
    resetPasswordHandle(state, { payload }) {
      return {
        ...state,
        status: payload.status,
        errorMessage: payload.errorMessage,
      };
    },
    getLoginQRHandle(state, { payload }) {
      return {
        ...state,
        status: 'showQR',
        randomUUID: payload.randomUUID,
        imgData: payload.imgData,
      };
    },
    checkLoginQRHandle(state, { payload }) {
      return {
        ...state,
        status: 'redirect',
        redirectURL: payload,
      };
    },
  },
};
