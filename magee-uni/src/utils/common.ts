
/**
 * 参数处理 {name: 'a1', date: '2020-10-15'} => name=a1&date=2020-10-15
 * 
 * @param {any} param 
 * @return 
  */
export function transParams(param, parentKey = '') {
	const parts = [];

	for (const [key, value] of Object.entries(param)) {
		const currentKey = parentKey
			? Array.isArray(param) ? `${parentKey}[${key}]` : `${parentKey}.${key}`
			: key;

		if (value === null || value === undefined) continue; // 跳过空值

		if (typeof value === 'object') {
			parts.push(transParams(value, currentKey));      // 递归
		} else {
			parts.push(`${encodeURIComponent(currentKey)}=${encodeURIComponent(value as string)}`);
		}
	}
	return parts.join('&');
}