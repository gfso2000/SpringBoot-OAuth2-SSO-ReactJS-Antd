import { stringify } from 'qs';
import request from '@/utils/request';

export async function fakeRegister(params) {
  return request('/api/register', {
    method: 'POST',
    body: params,
  });
}

export async function fakeResetPassword(params) {
  const { userName } = params;
  return request(`/api/resetpassword?userName=${userName}`, {
    method: 'POST',
  });
}

export async function fakeGetLoginQR() {
  return request('/qrLogin/getQr', {
    method: 'GET',
  });
}

export async function fakeCheckLoginQR(params) {
  const { randomUUID } = params;
  return request(`/qrLogin/check?uuid=${randomUUID}`, {
    method: 'GET',
  });
}

