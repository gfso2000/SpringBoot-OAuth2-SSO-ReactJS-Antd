import { fakeEditProfile,fakeGetUserName } from '@/services/apiServices';
import {delay} from '@/utils/request';

export default {
  namespace: 'register',

  state: {
    status: undefined,
  },

  effects: {
    *submit({ payload }, { call, put }) {
      const response = yield call(fakeEditProfile, payload);
      yield put({
        type: 'registerHandle',
        payload: response,
      });
    },
    *getUserName({ payload }, { call, put }) {
      const response = yield call(fakeGetUserName, payload);
      yield put({
        type: 'getUserNameHandle',
        payload: response,
      });
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
        status: 'editprofile_'+payload.status,
        errorMessage: payload.errorMessage,
      };
    },
    getUserNameHandle(state, { payload }) {
      return {
        ...state,
        status: 'getusername_'+payload.status,
        errorMessage: payload.errorMessage,
      };
    },
  },
};
