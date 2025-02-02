export const handleDate = (dateTime) => {// Kiểm tra nếu dateTime là UTC
  const isUTC = dateTime && dateTime.includes('Z') && dateTime.includes('T');

  if (!isUTC) return dateTime;

  const optionsDate = { year: 'numeric', month: '2-digit', day: '2-digit' };
  const optionsTime = { hour: '2-digit', minute: '2-digit', second: '2-digit' };
  const date = new Date(dateTime);

  const localDate = date.toLocaleDateString(undefined, optionsDate);
  const localTime = date.toLocaleTimeString(undefined, optionsTime);

  return `${localDate.split('/').reverse().join('-')} ${localTime}`;
};

