export async function apiRequest(url, options = {}) {
  try {
    const csrfHeaders = {};
    if (window.csrf?.name && window.csrf?.value) {
      csrfHeaders[window.csrf.name] = window.csrf.value;
    }

    const response = await fetch(url, {
      ...options,
      headers: {
        'Accept': 'application/json',
        ...(options.body instanceof FormData ? {} : { 'Content-Type': 'application/json' }), // FormData가 아닌 경우 json으로
        ...csrfHeaders,
        ...options.headers
      },
      credentials: 'same-origin'
    });

    const result = await response.json();

    if (!result.success) {
      if (result.message) {
        alert(result.message);
      }

      if (result.redirectUrl) {
        window.location.href = result.redirectUrl;
      }

      throw new Error(result.message || 'API 요청 실패');
    }

    return result.data;

  } catch (error) {
    console.error('API Error:', error);

    if (error.message === 'Failed to fetch') {
      alert('서버와의 연결에 실패했습니다.');
    }

    throw error;
  }
}

export async function apiGet(url) {
  return apiRequest(url, { method: 'GET' });
}

export async function apiPost(url, data) {
  return apiRequest(url, {
    method: 'POST',
    body: JSON.stringify(data)
  });
}

export async function apiPostForm(url, formData = {}) {
  return apiRequest(url, {
    method: 'POST',
    body: formData,
  });
}
export async function apiPut(url, data) {
  return apiRequest(url, {
    method: 'PUT',
    body: JSON.stringify(data)
  });
}

export async function apiDelete(url) {
  return apiRequest(url, { method: 'DELETE' });
}