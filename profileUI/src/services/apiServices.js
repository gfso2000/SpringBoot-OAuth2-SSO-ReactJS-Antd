import { stringify } from 'qs';
import request from '@/utils/request';

export async function fakeEditProfile(params) {
  return request('/api/editprofile', {
    method: 'POST',
    body: params,
  });
}

export async function fakeGetUserName(params) {
  return request('/api/getusername', {
    method: 'GET',
    body: params,
  });
}
